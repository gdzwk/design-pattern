**Apache HttpComponents 4.5.X 教程** [链接](http://hc.apache.org/httpcomponents-client-4.5.x/tutorial/html/index.html)

[TOC]



# 前言

超文本传输协议 ( HTTP ) 可能是当今Internet上使用的最重要的协议。Web服务，具有网络功能的设备以及网络计算的增长继续将HTTP协议的作用扩展到用户驱动的Web浏览器之外，同时增加了需要HTTP支持的应用程序的数量。

尽管java.net软件包提供了用于通过HTTP访问资源的基本功能，但它并未提供许多应用程序所需的全部灵活性或功能。HttpClient试图通过提供一个高效，最新且功能丰富的软件包来填补这一空白，以实现最新HTTP标准和建议的客户端。

HttpClient是为扩展而设计的，同时提供了对基本HTTP协议的强大支持，对于构建HTTP感知的客户端应用程序（例如Web浏览器，Web服务客户端或利用或扩展HTTP协议进行分布式通信的系统）的任何人来说，HttpClient都是值得一试的。

1. **HttpClient 适用范围**
   * 基于[HttpCore](http://hc.apache.org/httpcomponents-core/index.html)的客户端HTTP传输库
   * 基于经典 ( 阻塞 ) I / O
   * 内容不可知 ( Content agnostic - 是否翻译为底层封装无感知更好 )
2. **HttpClient 不适用范围**
   * HttpClient不是浏览器。它是客户端HTTP传输库。HttpClient的目的是发送和接收HTTP消息。HttpClient不会尝试处理内容，执行HTML页面中嵌入的javascript，尝试猜测内容类型 ( 如果未显式设置 )，重新格式化请求/重写位置URI或其他与HTTP传输无关的功能。



# 1  基本原理

## 1.1  请求执行

 HttpClient的最基本功能是执行HTTP方法。HTTP方法的执行涉及一个或多个HTTP请求/ HTTP响应交换，通常由HttpClient在内部处理。期望用户提供要执行的请求对象，并且HttpClient希望将请求传输到目标服务器，以返回相应的响应对象，如果执行不成功，则抛出异常。

很自然，HttpClient API的主要入口点是定义上述协定的HttpClient接口。

这是最简单形式的请求执行过程示例 :

```java
CloseableHttpClient httpclient = HttpClients.createDefault();
HttpGet httpget = new HttpGet("http://localhost/");
CloseableHttpResponse response = httpclient.execute(httpget);
try {
    <...>
} finally {
    response.close();
}
```



### 1.1.1  Http Request

所有HTTP请求的请求行均包含方法名称，请求URI和HTTP协议版本。

HttpClient开箱即用地支持HTTP / 1.1规范中定义的所有HTTP方法 : `GET`、`HEAD`、`POST`、`PUT`、`DELETE`、`TRACE` 和 `OPTIONS`。每种方法类型都有一个特定的类 : `HttpGet`、`HttpHead`、`HttpPost`、`HttpPut`、`HttpDelete`、`HttpTrace` 和 `HttpOptions`。

Request-URI是一个统一资源标识符，用于标识要在其上应用请求的资源。HTTP请求URI由协议方案，主机名，可选端口，资源路径，可选查询和可选片段组成。

```java
HttpGet httpGet = new HttpGet(
     "http://www.google.com/search?hl=en&q=httpclient&btnG=Google+Search&aq=f&oq=");
```

HttpClient提供 `URIBuilder` 实用程序类，以简化请求URI的创建和修改。

```java
URI uri = new URIBuilder()
        		.setScheme("http")
        		.setHost("www.google.com")
        		.setPath("/search")
        		.setParameter("q", "httpclient")
        		.setParameter("btnG", "Google Search")
        		.setParameter("aq", "f")
        		.setParameter("oq", "")
        		.build();
HttpGet httpget = new HttpGet(uri);
System.out.println(httpget.getURI());
```

stdout >

```
http://www.google.com/search?q=httpclient&btnG=Google+Search&aq=f&oq=
```



### 1.1.2  Http Response

HTTP响应是服务器在收到并解释了请求消息后将其发送回客户端的消息。该消息的第一行包含协议版本，后跟数字状态代码及其关联的文本短语。

```java
HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 
											  HttpStatus.SC_OK, 
                                              "OK");
System.out.println(response.getProtocolVersion());
System.out.println(response.getStatusLine().getStatusCode());
System.out.println(response.getStatusLine().getReasonPhrase());
System.out.println(response.getStatusLine().toString());
```

stdout >

```
HTTP/1.1
200
OK
HTTP/1.1 200 OK
```



### 1.1.3  使用请求头

HTTP消息可以包含许多描述消息属性的请求头，例如内容长度、内容类型等。HttpClient提供了检索、添加、删除和枚举请求头的方法。

```java
HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 
    HttpStatus.SC_OK, "OK");
response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");
Header h1 = response.getFirstHeader("Set-Cookie");
System.out.println(h1);
Header h2 = response.getLastHeader("Set-Cookie");
System.out.println(h2);
Header[] hs = response.getHeaders("Set-Cookie");
System.out.println(hs.length);
```

stdout >

```
Set-Cookie: c1=a; path=/; domain=localhost
Set-Cookie: c2=b; path="/", c3=c; domain="localhost"
2
```

获取给定类型的所有请求头的最有效方法是使用 `HeaderIterator` 接口。

```java
HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 
    HttpStatus.SC_OK, "OK");
response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");

HeaderIterator it = response.headerIterator("Set-Cookie");

while (it.hasNext()) {
    System.out.println(it.next());
}
```

stdout >

```
Set-Cookie: c1=a; path=/; domain=localhost
Set-Cookie: c2=b; path="/", c3=c; domain="localhost"
```

它还提供了方便的方法来将HTTP消息解析为单独的请求头元素。

```java
HttpResponse response = new BasicHttpResponse(HttpVersion.HTTP_1_1, 
    HttpStatus.SC_OK, "OK");
response.addHeader("Set-Cookie", "c1=a; path=/; domain=localhost");
response.addHeader("Set-Cookie", "c2=b; path=\"/\", c3=c; domain=\"localhost\"");

HeaderElementIterator it = new BasicHeaderElementIterator(
    response.headerIterator("Set-Cookie"));

while (it.hasNext()) {
    HeaderElement elem = it.nextElement(); 
    System.out.println(elem.getName() + " = " + elem.getValue());
    NameValuePair[] params = elem.getParameters();
    for (int i = 0; i < params.length; i++) {
        System.out.println(" " + params[i]);
    }
}
```

stdout >

```
c1 = a
path=/
domain=localhost
c2 = b
path=/
c3 = c
domain=localhost
```



### 1.1.4  Http Entity

HTTP消息可以包含与请求或响应关联的内容实体。 实体可以在某些请求和响应中找到，因为它们是可选的。使用实体的请求称为实体封装请求。HTTP规范定义了两种封装请求方法的实体 : `POST` 和 `PUT`。通常期望响应包含内容实体。此规则有例外，例如对 `HEAD` 方法的响应和 `204 No Content`、`304 Not Modified`、`205 Reset Content` 响应。

HttpClient根据它们的内容来源区分三种实体 :

* **流式** : 内容是从流中接收的，或者是即时生成的。特别是，此类别包括从HTTP响应接收的实体。流式实体通常不可重复。
* **自包含** : 内容在内存中或通过独立于连接或其他实体的方式获得。自包含的实体通常是可重复的。这种类型的实体将主要用于封装HTTP请求的实体。
* **包装** : 内容是从另一个实体获得的。

当从HTTP响应流中传输内容时，此区别对于连接管理很重要。对于由应用程序创建且仅使用HttpClient发送的请求实体，流式和自包含式之间的区别并不重要。在这种情况下，建议将不可重复的实体视为流，将可重复的实体视为独立的。



#### 1.1.4.1  可重复实体

实体可以是可重复的，这意味着其内容可以被读取多次。这仅适用于自包含的实体 ( 例如 `ByteArrayEntity` 或 `StringEntity` )。



#### 1.1.4.2  使用Http实体

由于实体既可以表示二进制内容又可以表示字符内容，因此它支持字符编码 ( 以支持后者，即字符内容 )。

当执行带有封闭内容的请求时，或者当请求成功时，将创建实体，并使用响应主体将结果发送回客户端。

要从实体中读取内容，可以通过 `HttpEntity#getContent()` 方法检索输入流，该方法返回一个 `java.io.InputStream`，或者可以将输出流提供给 `HttpEntity#writeTo(OutputStream)` 方法， 一旦所有内容都已写入给定流，它将返回。

当收到传入消息的实体时，方法 `HttpEntity#getContentType()` 和 `HttpEntity#getContentLength()` 方法可用于读取常见的元数据，例如 `Content-Type` 和 `Content-Length` 请求头 ( 如果可用 )。由于 `Content-Type` 请求头可以包含文本 `MIME` 类型 ( 例如 `text/plain` 或 `text/html` ) 的字符编码，因此使用 `HttpEntity#getContentEncoding()` 方法读取此信息。如果请求头不可用，则将返回-1的长度，并且内容类型为NULL。如果 `Content-Type` 请求头可用，则将返回 `Header` 对象。

在为外发消息创建实体时，此元数据必须由实体的创建者提供。

```java
StringEntity myEntity = new StringEntity("important message", 
   ContentType.create("text/plain", "UTF-8"));

System.out.println(myEntity.getContentType());
System.out.println(myEntity.getContentLength());
System.out.println(EntityUtils.toString(myEntity));
System.out.println(EntityUtils.toByteArray(myEntity).length);
```

stdout >

```
Content-Type: text/plain; charset=utf-8
17
important message
17
```



### 1.1.5  确保释放底层资源

为了确保正确释放系统资源，必须关闭与实体关联的内容流或响应本身。

```java
CloseableHttpClient httpclient = HttpClients.createDefault();
HttpGet httpget = new HttpGet("http://localhost/");
CloseableHttpResponse response = httpclient.execute(httpget);
try {
    HttpEntity entity = response.getEntity();
    if (entity != null) {
        InputStream instream = entity.getContent();
        try {
            // do something useful
        } finally {
            instream.close();
        }
    }
} finally {
    response.close();
}
```

关闭内容流和关闭响应之间的区别在于，前者将通过消费实体内容来尝试使基础连接保持活动状态，而后者立即关闭并丢弃该连接。

请注意，一旦使用 `HttpEntity#writeTo(OutputStream)` 方法完全写完实体后，要确保正确释放系统资源。如果调用 `HttpEntity#getContent()` 获得 `java.io.InputStream` 的实例，则还应在finally子句中关闭流。

使用流式实体时，可以使用 `EntityUtils#consume(HttpEntity)` 方法来确保实体内容已被完全消耗，并且基础流已关闭。

但是在某些情况下，可能只需要检索整个响应内容的一小部分，而消耗剩余内容并使连接可重用的性能代价太高，在这种情况下，可以通过关闭响应来终止内容流。

```java
CloseableHttpClient httpclient = HttpClients.createDefault();
HttpGet httpget = new HttpGet("http://localhost/");
CloseableHttpResponse response = httpclient.execute(httpget);
try {
    HttpEntity entity = response.getEntity();
    if (entity != null) {
        InputStream instream = entity.getContent();
        int byteOne = instream.read();
        int byteTwo = instream.read();
        // Do not need the rest
    }
} finally {
    response.close();
}
```

连接将不会被重用，但是它所拥有的所有级别资源都将被正确地释放。

> 疑问，如何进行连接重用呢？



### 1.1.6  消费实体内容

推荐使用实体内容的方法是使用其 `HttpEntity#getContent()` 或 `HttpEntity#writeTo(OutputStream)` 方法。HttpClient还带有 `EntityUtils` 类，该类公开了一些静态方法以更轻松地从实体中读取内容或信息。不必直接读取 `java.io.InputStream`，而是可以使用此类的方法来检索字符串/字节数组中的整个内容主体。但是，强烈建议不要使用 `EntityUtils`，除非响应实体源自受信任的HTTP服务器并且已知长度有限。

```java
CloseableHttpClient httpclient = HttpClients.createDefault();
HttpGet httpget = new HttpGet("http://localhost/");
CloseableHttpResponse response = httpclient.execute(httpget);
try {
    HttpEntity entity = response.getEntity();
    if (entity != null) {
        long len = entity.getContentLength();
        if (len != -1 && len < 2048) {
            System.out.println(EntityUtils.toString(entity));
        } else {
            // Stream content out
        }
    }
} finally {
    response.close();
}
```

在某些情况下，可能需要多次读取实体内容。在这种情况下，实体内容必须以某种方式缓冲在内存或磁盘中。最简单的方法是使用 `BufferedHttpEntity` 类包装原始实体。这将导致原始实体的内容被读入内存缓冲区中。在所有其他方式中，实体包装器将具有原始包装器。

```java
CloseableHttpResponse response = <...>
HttpEntity entity = response.getEntity();
if (entity != null) {
    entity = new BufferedHttpEntity(entity);
}
```



### 1.1.7  生产实体内容

HttpClient提供了几个类，可用于通过HTTP连接有效地流式传输内容。这些类的实例可以与诸如 `POST` 和 `PUT` 之类的实体封装请求相关联，以便将实体内容封装到传出的HTTP请求中。HttpClient为大多数常见的数据容器 ( 例如字符串、字节数组、输入流和文件 ) 提供了几个类 : `StringEntity`、`ByteArrayEntity`、`InputStreamEntity` 和 `FileEntity`。

```java
File file = new File("somefile.txt");
FileEntity entity = new FileEntity(file, ContentType.create("text/plain", "UTF-8"));     

HttpPost httppost = new HttpPost("http://localhost/action.do");
httppost.setEntity(entity);
```

请注意，`InputStreamEntity` 不可重复，因为它只能从基础数据流中读取一次。通常，建议实现一个自包含的自定义 `HttpEntity` 类，而不是使用通用 `InputStreamEntity`。`FileEntity` 可以是一个很好的起点。



#### 1.1.7.1  Html 表单

许多应用程序需要模拟提交HTML表单的过程，例如登录Web应用程序或提交输入数据。HttpClient提供了实体类 `UrlEncodedFormEntity` 来简化此过程。

```java
List<NameValuePair> formparams = new ArrayList<NameValuePair>();
formparams.add(new BasicNameValuePair("param1", "value1"));
formparams.add(new BasicNameValuePair("param2", "value2"));
UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
HttpPost httppost = new HttpPost("http://localhost/handler.do");
httppost.setEntity(entity);
```

`UrlEncodedFormEntity` 实例将使用所谓的URL编码来编码参数并产生以下内容 :

```
param1=value1&param2=value2
```



#### 1.1.7.2  内容分块

通常，建议让HttpClient根据要传输的HTTP消息的属性选择最合适的传输编码。但是，可以通过将 `HttpEntity#setChunked()` 设置为true来通知HttpClient首选块编码。请注意，HttpClient仅将此标志用作提示。当使用不支持块编码的HTTP协议版本 ( 例如HTTP / 1.0 ) 时，将忽略此值。

```java
StringEntity entity = new StringEntity("important message",
        ContentType.create("plain/text", Consts.UTF_8));
entity.setChunked(true);
HttpPost httppost = new HttpPost("http://localhost/acrtion.do");
httppost.setEntity(entity);
```



### 1.1.8  Response 处理

处理响应的最简单，最方便的方法是使用 `ResponseHandler` 接口，该接口包括 `handleResponse(HttpResponse response)` 方法。这种方法完全使用户不必担心连接管理。使用 `ResponseHandler` 时，无论请求执行成功还是导致异常，HttpClient都会自动确保将连接释放回连接管理器。

```java
CloseableHttpClient httpclient = HttpClients.createDefault();
HttpGet httpget = new HttpGet("http://localhost/json");

ResponseHandler<MyJsonObject> rh = new ResponseHandler<MyJsonObject>() {

    @Override
    public JsonObject handleResponse(
            final HttpResponse response) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(
                    statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
        if (entity == null) {
            throw new ClientProtocolException("Response contains no content");
        }
        Gson gson = new GsonBuilder().create();
        ContentType contentType = ContentType.getOrDefault(entity);
        Charset charset = contentType.getCharset();
        Reader reader = new InputStreamReader(entity.getContent(), charset);
        return gson.fromJson(reader, MyJsonObject.class);
    }
};
MyJsonObject myjson = client.execute(httpget, rh);
```



## 1.2  HttpClient 接口

HttpClient接口代表HTTP请求执行的最基本协定。它不对请求执行过程施加任何限制或特定细节，并且将连接管理，状态管理，身份验证和重定向处理的细节留给各个实现。这将使用附加功能 ( 如响应内容缓存 ) 装饰接口变得更容易。

通常，HttpClient实现充当许多专用处理程序或策略接口实现的基础，这些处理程序或策略接口实现负责处理HTTP协议的特定方面，例如重定向或身份验证处理，或做出有关连接持久性和保持生存期的决策。这使用户能够用自定义的，特定于应用程序的方面有选择地替换那些方面的默认实现。

```java
ConnectionKeepAliveStrategy keepAliveStrat = new DefaultConnectionKeepAliveStrategy() {

    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        long keepAlive = super.getKeepAliveDuration(response, context);
        if (keepAlive == -1) {
            // Keep connections alive 5 seconds if a keep-alive value
            // has not be explicitly set by the server
            keepAlive = 5000;
        }
        return keepAlive;
    }

};
CloseableHttpClient httpclient = HttpClients.custom().setKeepAliveStrategy(keepAliveStrat).build();
```



### 1.2.1  HttpClient 线程安全

HttpClient实现应该是线程安全的。建议将此类的同一实例重用于多个请求执行。



### 1.2.2  HttpClient 资源释放

当不再需要实例 `CloseableHttpClien` 且即将超出范围时，必须通过调用 `CloseableHttpClient#close()` 方法关闭与之关联的连接管理器。

```java
CloseableHttpClient httpclient = HttpClients.createDefault();
try {
    <...>
} finally {
    httpclient.close();
}
```



### 1.3  Http执行上下文

最初，HTTP被设计为无状态，面向响应请求的协议。但是，现实世界中的应用程序通常需要能够通过几次逻辑相关的请求-响应交换来保留状态信息。为了使应用程序能够维持处理状态，HttpClient允许HTTP请求在称为HTTP上下文的特定执行上下文中执行。如果在连续请求之间重用相同的上下文，则多个逻辑相关的请求可以参与逻辑会话。HTTP上下文的功能类似于 `java.util.Map<String, Object>`。它只是任意命名值的集合。应用程序可以在请求执行之前填充上下文属性，或者在执行完成之后检查上下文。

`HttpContext` 可以包含任意对象，因此在多个线程之间共享可能不安全。建议每个执行线程都维护自己的上下文。

在HTTP请求执行过程中，`HttpClient` 将以下属性添加到执行上下文 :

* `HttpConnection` 实例，表示与目标服务器的实际连接。
* `HttpHost` 实例，表示连接目标。
* `HttpRoute` 实例，表示完整连接路由。
* `HttpRequest` 实例，表示实际http请求。执行上下文中的最终 `HttpRequest` 对象始终表示消息的状态，就像发送到目标服务器一样。默认情况下，HTTP/1.0和HTTP/1.1使用相对请求URI。但是，如果请求是通过代理以非隧道模式发送的，则URI将是绝对的。
* `HttpReponse` 实例，表示实际http响应。
* `java.lang.Boolean` 对象，表示标志，该标志指示实际请求是否已完全发送到连接目标。
* `RequestConfig` 实例，表示实际请求配置。
* `java.util.List<URI>` 对象，表示在请求执行过程中收到的所有重定向位置的集合。

可以使用 `HttpClientContext` 适配器类简化与上下文状态的交互。

```java
HttpContext context = <...>
HttpClientContext clientContext = HttpClientContext.adapt(context);
HttpHost target = clientContext.getTargetHost();
HttpRequest request = clientContext.getRequest();
HttpResponse response = clientContext.getResponse();
RequestConfig config = clientContext.getRequestConfig();
```

代表逻辑相关会话的多个请求序列应使用同一 `HttpContext` 实例执行，以确保请求之间的会话上下文和状态信息自动传播。

在以下示例中，由初始请求设置的请求配置将保留在执行上下文中，并传播到共享相同上下文的连续请求中。

```java
CloseableHttpClient httpclient = HttpClients.createDefault();
RequestConfig requestConfig = RequestConfig.custom()
        .setSocketTimeout(1000)
        .setConnectTimeout(1000)
        .build();

HttpGet httpget1 = new HttpGet("http://localhost/1");
httpget1.setConfig(requestConfig);
CloseableHttpResponse response1 = httpclient.execute(httpget1, context);
try {
    HttpEntity entity1 = response1.getEntity();
} finally {
    response1.close();
}
HttpGet httpget2 = new HttpGet("http://localhost/2");
CloseableHttpResponse response2 = httpclient.execute(httpget2, context);
try {
    HttpEntity entity2 = response2.getEntity();
} finally {
    response2.close();
}
```



## 1.4  Http 协议拦截器

HTTP协议拦截器是一种实现HTTP协议特定方面的例程。通常，协议拦截器应作用于传入消息的一个特定请求头或一组相关请求头，或使用一个特定请求头或一组相关请求头填充输出消息。协议拦截器还可以操纵消息中包含的内容实体-透明的内容压缩/解压缩就是一个很好的例子。通常，这是通过使用 "装饰器" 模式完成的，在该模式中，使用包装实体类来装饰原始实体。几个协议拦截器可以组合在一起形成一个逻辑单元。

协议拦截器可以通过HTTP执行上下文共享信息 ( 例如处理状态 ) 来进行协作。协议拦截器可以使用HTTP上下文来存储一个请求或几个连续请求的处理状态。

通常，拦截器的执行顺序无关紧要，只要它们不依赖于执行上下文的特定状态即可。如果协议拦截器因具有相互依赖性，必须按特定的顺序执行，则应按照与预期的执行顺序相同的顺序将它们添加到协议处理器。

协议拦截器必须实现为线程安全的。与Servlet相似，协议拦截器不应使用实例变量，除非对这些变量的访问已同步。

这是一个示例，说明如何使用本地上下文在连续的请求之间保留处理状态 :

```java
CloseableHttpClient httpclient = HttpClients.custom()
        .addInterceptorLast(new HttpRequestInterceptor() {

            public void process(final HttpRequest request, final HttpContext context) 
                	throws HttpException, IOException {
                AtomicInteger count = (AtomicInteger) context.getAttribute("count");
                request.addHeader("Count", Integer.toString(count.getAndIncrement()));
            }

        })
        .build();

AtomicInteger count = new AtomicInteger(1);
HttpClientContext localContext = HttpClientContext.create();
localContext.setAttribute("count", count);

HttpGet httpget = new HttpGet("http://localhost/");
for (int i = 0; i < 10; i++) {
    CloseableHttpResponse response = httpclient.execute(httpget, localContext);
    try {
        HttpEntity entity = response.getEntity();
    } finally {
        response.close();
    }
}
```



## 1.5  异常处理

HTTP协议处理器可以引发两种类型的异常 : 如果发生 I/O 故障 ( 例如套接字超时或套接字重置 )，则抛出 `java.io.IOException`; 以及发出HTTP失败信号 ( 例如违反HTTP协议 ) 的 `HttpException`。通常，I/O 错误被认为是非致命错误且可恢复，而HTTP协议错误被认为是致命错误，无法自动从中恢复。请注意，HttpClient实现将 `HttpExceptions` 重新抛出为 `ClientProtocolException`，它是 `java.io.IOException` 的子类。这使HttpClient的用户可以从单个catch子句中处理 I/O 错误和协议违规。



### 1.5.1 Http 传输安全

重要的是要了解HTTP协议并不适合所有类型的应用程序。HTTP是一种简单的面向请求/响应的协议，最初旨在支持静态或动态生成的内容检索。它从未打算支持事务操作。例如，如果HTTP服务器成功接收和处理请求，生成响应并将状态代码发送回客户端，则它将认为已履行合同的一部分。如果客户端由于读取超时，请求取消或系统崩溃而无法完整接收响应，则服务器将不尝试回滚事务。如果客户端决定重试相同的请求，则服务器将不可避免地结束多次执行同一事务。在某些情况下，这可能导致应用程序数据损坏或应用程序状态不一致。

即使**HTTP从未被设计为支持事务处理**，但只要满足某些条件，它仍可以用作关键任务应用程序的传输协议。为了确保HTTP传输层的安全性，系统必须**确保HTTP方法在应用程序层上的幂等性**。



### 1.5.2  幂等方法

 HTTP / 1.1规范将幂等方法定义为【方法还可以具有“幂等”的特性，因为 ( 错误或过期问题除外 ) N> 0个相同请求的副作用与单个请求的副作用相同】。  

换句话说，应用程序应确保准备好应对同一方法的多次执行所带来的影响。例如，这可以通过提供唯一的事务标识以及通过其他避免执行相同逻辑操作的方式来实现。

请注意，此问题并非特定于HttpClient。基于浏览器的应用程序会遇到与HTTP方法非幂等性完全相同的问题。

默认情况下，HttpClient仅假定非实体封装方法 ( 例如 `GET` 和 `HEAD` ) 是幂等的，而实体封装方法 ( 例如 `POST` 和 `PUT` ) 出于兼容性原因而不是。



### 1.5.3  自动异常恢复

默认情况下，HttpClient尝试自动从 I/O 异常中恢复。默认的自动恢复机制仅限于一些已知安全的异常。

* HttpClient不会尝试从任何逻辑或HTTP协议错误 ( 从 `HttpException` 类派生的错误 ) 中恢复。
* **HttpClient将自动重试那些被认为是幂等的方法。**
* 当HTTP请求仍在传输到目标服务器 ( 即请求尚未完全传输到服务器 ) 时，HttpClient会自动重试那些由于传输异常而失败的方法。



### 1.5.4  请求重试处理

为了启用自定义异常恢复机制，应该提供 `HttpRequestRetryHandler` 接口的实现。

```java
HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

    public boolean retryRequest(IOException exception, int executionCount, 
                                HttpContext context) {
        if (executionCount >= 5) {
            // Do not retry if over max retry count
            return false;
        }
        if (exception instanceof InterruptedIOException) {
            // Timeout
            return false;
        }
        if (exception instanceof UnknownHostException) {
            // Unknown host
            return false;
        }
        if (exception instanceof ConnectTimeoutException) {
            // Connection refused
            return false;
        }
        if (exception instanceof SSLException) {
            // SSL handshake exception
            return false;
        }
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = clientContext.getRequest();
        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
        if (idempotent) {
            // Retry if the request is considered idempotent
            return true;
        }
        return false;
    }

};
CloseableHttpClient httpclient = HttpClients.custom().setRetryHandler(myRetryHandler).build();
```

请注意，为了将RFC-2616定义为幂等的那些请求方法视为可以自动重试的安全方法，可以使用 `StandardHttpRequestRetryHandler` 而不是默认使用的方法 : `GET`、`HEAD`、`PUT`、`DELETE`、`OPTIONS` 和 `TRACE`。



## 1.6  终止请求

在某些情况下，由于目标服务器上的高负载或客户端发出的并发请求太多，HTTP请求执行无法在预期的时间内完成。在这种情况下，可能有必要提前终止请求并解除对在 I/O 操作中被阻塞的执行线程的阻塞。可以通过调用 `HttpUriRequest#abort()` 方法在执行的任何阶段中止由HttpClient执行的HTTP请求。此方法是线程安全的，可以从任何线程调用。当HTTP请求中止时，其执行线程-即使当前在 I/O 操作中被阻塞-也可以通过抛出 `InterruptedIOException` 来保证解除阻塞。



## 1.7  重定向处理

HttpClient自动处理所有类型的重定向，但HTTP规范明确禁止需要用户干预的重定向除外。请参阅其他 ( 状态码303 )，将 `POST` 上的重定向和 `PUT` 请求转换为HTTP规范要求的 `GET` 请求。可以使用自定义重定向策略来放宽HTTP规范对 `POST` 方法的自动重定向的限制。

```java
LaxRedirectStrategy redirectStrategy = new LaxRedirectStrategy();
CloseableHttpClient httpclient = HttpClients.custom()
        								 .setRedirectStrategy(redirectStrategy)
        								 .build();
```

HttpClient在执行过程中经常必须重写请求消息。默认情况下，HTTP/1.0 和 HTTP/1.1 通常使用相对请求URI。同样，原始请求可能会多次从位置重定向到另一个位置。可以使用原始请求和上下文来构建最终解释的绝对HTTP位置。实用程序方法 `URIUtils#resolve` 可用于构建用于生成最终请求的解释后的绝对URI。此方法包括重定向请求或原始请求中的最后一个片段标识符。

```java
CloseableHttpClient httpclient = HttpClients.createDefault();
HttpClientContext context = HttpClientContext.create();
HttpGet httpget = new HttpGet("http://localhost:8080/");
CloseableHttpResponse response = httpclient.execute(httpget, context);
try {
    HttpHost target = context.getTargetHost();
    List<URI> redirectLocations = context.getRedirectLocations();
    URI location = URIUtils.resolve(httpget.getURI(), target, redirectLocations);
    System.out.println("Final HTTP location: " + location.toASCIIString());
    // Expected to be an absolute URI
} finally {
    response.close();
}
```



# 2  连接管理

## 2.1  连接持久性

建立从一台主机到另一台主机的连接的过程非常复杂，并且涉及两个端点之间的多次数据包交换，这可能会非常耗时。连接握手的开销可能很大，尤其是对于小型HTTP消息而言。如果可以将打开的连接重新用于执行多个请求，则可以获得更高的数据吞吐量。

HTTP/1.1 指出，默认情况下，HTTP连接可以重复用于多个请求。符合 HTTP/1.0 的端点还可以使用一种机制来显式地传达其首选项，以使连接保持活动状态并将其用于多个请求。HTTP代理还可以在一段时间内保持空闲连接处于活动状态，以防后续请求需要连接到同一目标主机。使连接保持活动的能力通常称为连接持久性。HttpClient完全支持连接持久性。



## 2.2  Http 连接路由

HttpClient能够直接或通过可能涉及多个中间连接 ( 也称为跳跃点 ) 的路由建立到目标主机的连接。HttpClient将路由连接区分为普通连接、隧道连接和分层连接。使用多个中间代理来连接到目标主机的过程被称为代理链接。

通过连接到目标或第一个也是唯一的代理来建立普通路由。通过连接到第一个隧道并通过代理链到目标隧道来建立隧道路由。没有代理的路由无法建立隧道。通过在现有连接上对协议进行分层来建立分层路由。协议只能在通向目标的隧道上或在没有代理的直接连接上分层。



### 2.2.1  路由计算

`RouteInfo` 接口表示有关到目标主机的确定路由的信息，涉及到一个或多个中间步骤或跳跃点。`HttpRoute` 是 `RouteInfo` 的具体实现，不能更改 ( 不可变 )。`HttpTracker` 是一个可变的 `RouteInfo` 实现，供HttpClient在内部使用，以跟踪到最终路由目标的其余跳跃点。成功执行指向路由目标的下一跳后，可以更新 `HttpTracker`。`HttpRouteDirector` 是一个帮助程序类，可用于计算路由的下一步。此类在HttpClient内部使用。

`HttpRoutePlanner` 是一个接口，表示基于执行上下文计算到给定目标的完整路由的策略。HttpClient附带了两个默认的 `HttpRoutePlanner` 实现。`SystemDefaultRoutePlanner` 基于 `java.net.ProxySelector`。默认情况下，它将从系统属性或运行应用程序的浏览器中获取JVM的代理设置。`DefaultProxyRoutePlanner` 实现不使用任何Java系统属性，也不使用任何系统或浏览器代理设置。它总是通过相同的默认代理来计算路由。



### 2.2.2  安全的HTTP连接

如果未经授权的第三方无法读取或篡改两个连接端点之间传输的信息，则认为HTTP连接是安全的。SSL/TLS 协议是确保HTTP传输安全性的最广泛使用的技术。但是，也可以采用其他加密技术。通常，HTTP传输位于 SSL/TLS 加密连接上。



## 2.3  Http 连接管理器

### 2.3.1  托管连接和连接管理器

HTTP连接是复杂的，有状态的，线程不安全的对象，需要对其进行适当的管理以使其正常运行。HTTP连接一次只能由一个执行线程使用。HttpClient使用一个特殊的实体来管理对HTTP连接的访问，该实体称为HTTP连接管理器，由 `HttpClientConnectionManager` 接口表示。HTTP连接管理器的目的是充当新HTTP连接的工厂，以管理持久连接的生命周期并同步对持久连接的访问，以确保一次只有一个线程可以访问连接。在内部，HTTP连接管理器使用 `ManagedHttpClientConnection` 实例作为实际连接的代理，该实例管理连接状态并控制 I/O 操作的执行。如果托管连接被其使用者释放或显式关闭，则基础连接将从其代理分离，并返回给管理器。即使服务使用者仍然拥有对代理实例的引用，它也不再能够有意或无意地执行任何 I/O 操作或更改实际连接的状态。

这是从连接管理器获取连接的示例 :

```java
HttpClientContext context = HttpClientContext.create();
HttpClientConnectionManager connMrg = new BasicHttpClientConnectionManager();
HttpRoute route = new HttpRoute(new HttpHost("localhost", 80));
// Request new connection. This can be a long process
ConnectionRequest connRequest = connMrg.requestConnection(route, null);
// Wait for connection up to 10 sec
HttpClientConnection conn = connRequest.get(10, TimeUnit.SECONDS);
try {
    // If not open
    if (!conn.isOpen()) {
        // establish connection based on its route info
        connMrg.connect(conn, route, 1000, context);
        // and mark it as route complete
        connMrg.routeComplete(conn, route, context);
    }
    // Do useful things with the connection.
} finally {
    connMrg.releaseConnection(conn, null, 1, TimeUnit.MINUTES);
}
```

如有必要，可以通过调用 `ConnectionRequest#cancel()` 提前终止连接请求。这将取消阻塞在 `ConnectionRequest#get()` 方法中的线程。



### 2.3.2  简单的连接管理器

`BasicHttpClientConnectionManager` 是一个简单的连接管理器，一次仅维护一个连接。即使此类是线程安全的，也应仅由一个执行线程使用。`BasicHttpClientConnectionManager` 将努力将连接重新用于具有相同路由的后续请求。但是，如果持久连接的路由与连接请求的路由不匹配，它将关闭现有连接并针对给定的路由重新打开它。如果已经分配了连接，则抛出 `java.lang.IllegalStateException`。

此连接管理器实现应在EJB容器内使用。



### 2.3.3  池化连接管理器

`PoolingHttpClientConnectionManager` 是一个更复杂的实现，它管理客户端连接池并能够处理来自多个执行线程的连接请求。连接是基于每个路由池的。通过在池中租用连接而不是创建全新的连接，可以为管理器在池中已经具有持久连接的路由请求提供服务。

`PoolingHttpClientConnectionManager` 维护每个路由的总连接数上限。默认情况下，此实现将为每个给定路由创建不超过2个并发连接，并且总共不超过20个连接。对于许多现实应用程序，这些限制可能证明过于严格，特别是如果它们使用HTTP作为其服务的传输协议。

此示例显示如何调整连接池参数 :

```java
PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
// Increase max total connection to 200
cm.setMaxTotal(200);
// Increase default max connection per route to 20
cm.setDefaultMaxPerRoute(20);
// Increase max connections for localhost:80 to 50
HttpHost localhost = new HttpHost("locahost", 80);
cm.setMaxPerRoute(new HttpRoute(localhost), 50);

CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
```



### 2.3.4  关闭连接管理器

当不再需要HttpClient实例并将其超出范围时，重要的是关闭其连接管理器，以确保关闭该管理器保持活动的所有连接并释放由这些连接分配的系统资源。

```java
CloseableHttpClient httpClient = <...>
httpClient.close();
```



## 2.4  多线程请求执行

当配备有 `PoolingClientConnectionManager` 之类的池连接管理器时，HttpClient可用于使用多个执行线程同时执行多个请求。

`PoolingClientConnectionManager` 将根据其配置分配连接。如果给定路由的所有连接都已经租用，则对连接的请求将阻塞，直到将连接释放回池中为止。通过将 `http.conn-manager.timeout` 设置为正值，可以确保连接管理器不会无限期地阻塞连接请求操作。如果在给定的时间段内无法满足连接请求，则将抛出 `ConnectionPoolTimeoutException`。

```java
PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

// URIs to perform GETs on
String[] urisToGet = {
    "http://www.domain1.com/",
    "http://www.domain2.com/",
    "http://www.domain3.com/",
    "http://www.domain4.com/"
};

// create a thread for each URI
GetThread[] threads = new GetThread[urisToGet.length];
for (int i = 0; i < threads.length; i++) {
    HttpGet httpget = new HttpGet(urisToGet[i]);
    threads[i] = new GetThread(httpClient, httpget);
}

// start the threads
for (int j = 0; j < threads.length; j++) {
    threads[j].start();
}

// join the threads
for (int j = 0; j < threads.length; j++) {
    threads[j].join();
}
```

尽管 `HttpClient` 实例是线程安全的，并且可以在多个执行线程之间共享，但强烈建议每个线程都维护自己的 `HttpContext` 专用实例。

```java
static class GetThread extends Thread {

    private final CloseableHttpClient httpClient;
    private final HttpContext context;
    private final HttpGet httpget;

    public GetThread(CloseableHttpClient httpClient, HttpGet httpget) {
        this.httpClient = httpClient;
        this.context = HttpClientContext.create();
        this.httpget = httpget;
    }

    @Override
    public void run() {
        try {
            CloseableHttpResponse response = httpClient.execute(
                    httpget, context);
            try {
                HttpEntity entity = response.getEntity();
            } finally {
                response.close();
            }
        } catch (ClientProtocolException ex) {
            // Handle protocol errors
        } catch (IOException ex) {
            // Handle I/O errors
        }
    }

}
```



## 2.5  连接驱逐策略

经典阻塞 I/O 模型的主要缺点之一是，只有当在 I/O 操作中阻塞时，网络套接字才能对 I/O 事件作出反应。当将连接释放回管理器时，它可以保持活动状态，但是无法监视套接字的状态并对任何 I/O 事件作出反应。如果连接在服务器端被关闭，则客户端连接将无法检测到连接状态的变化 ( 并且通过关闭其端部的套接字来适当地做出反应 )。

HttpClient尝试通过在使用连接执行HTTP请求之前测试该连接是否为“过时的”（不再有效，因为该连接已在服务器端关闭）来缓解此问题。 过时的连接检查不是100％可靠的。 对于空闲连接检查的唯一可行的解决方案是，如果不涉及每个套接字一个线程的模型，使用一个专用的监视线程，用于清除由于长时间不活动而被认为已过期的连接。监视线程可以定期调用 `ClientConnectionManager#closeExpiredConnections()` 方法以关闭所有过期的连接，并从池中逐出关闭的连接。它还可以选择调用 `ClientConnectionManager#closeIdleConnections()` 方法来关闭在给定时间内空闲的所有连接。

```java
public static class IdleConnectionMonitorThread extends Thread {
    
    private final HttpClientConnectionManager connMgr;
    private volatile boolean shutdown;
    
    public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
        super();
        this.connMgr = connMgr;
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // Close expired connections
                    connMgr.closeExpiredConnections();
                    // Optionally, close connections
                    // that have been idle longer than 30 sec
                    connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
                }
            }
        } catch (InterruptedException ex) {
            // terminate
        }
    }
    
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
    
}
```



## 2.6  持久连接策略

HTTP规范没有指定持久连接可以保持多长时间，并且应该保持有效状态。一些HttpClient使用一个非标准的 `Keep-Alive` 头来与客户机通信，这段时间以秒为单位，N个单位时间内在服务器端保持连接的活动性。HttpClient使用此信息 ( 如果可用 )。如果响应中不存在 `Keep-Alive` 标头，则HttpClient会假定连接可以无限期保持活动状态。但是，为了节省系统资源，许多常用的HTTP服务器被配置为在一定的不活动时间后删除持久连接，而通常不通知客户端。如果默认策略过于乐观，则可能需要提供一种自定义的 `Keep-Alive` 策略。

```java
ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {

    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        // Honor 'keep-alive' header
        HeaderElementIterator it = new BasicHeaderElementIterator(
                response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            HeaderElement he = it.nextElement();
            String param = he.getName();
            String value = he.getValue();
            if (value != null && param.equalsIgnoreCase("timeout")) {
                try {
                    return Long.parseLong(value) * 1000;
                } catch(NumberFormatException ignore) {
                }
            }
        }
        HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
        if ("www.naughty-server.com".equalsIgnoreCase(target.getHostName())) {
            // Keep alive for 5 seconds only
            return 5 * 1000;
        } else {
            // otherwise keep alive for 30 seconds
            return 30 * 1000;
        }
    }

};
CloseableHttpClient client = HttpClients.custom().setKeepAliveStrategy(myStrategy).build();
```



## 2.7  连接套接字工厂

HTTP连接在内部使用 `java.net.Socket` 对象来处理数据传输的跨线传输。但是，它们依靠 `ConnectionSocketFactory` 接口来创建，初始化和连接套接字。这使HttpClient的用户可以在运行时提供应用程序特定的套接字初始化代码。`PlainConnectionSocketFactory` 是用于创建和初始化普通 ( 未加密 ) 套接字的默认工厂。

创建套接字的过程以及将其连接到主机的过程是分离的，因此可以在连接操作被阻塞的同时关闭套接字。

```java
HttpClientContext clientContext = HttpClientContext.create();
PlainConnectionSocketFactory sf = PlainConnectionSocketFactory.getSocketFactory();
Socket socket = sf.createSocket(clientContext);
int timeout = 1000; //ms
HttpHost target = new HttpHost("localhost");
InetSocketAddress remoteAddress = new InetSocketAddress(
        InetAddress.getByAddress(new byte[] {127,0,0,1}), 80);
sf.connectSocket(timeout, socket, target, remoteAddress, null, clientContext);
```



### 2.7.1  安全套接字分层

`LayeredConnectionSocketFactory` 是 `ConnectionSocketFactory` 接口的扩展。分层套接字工厂能够创建在现有普通套接字上分层的套接字。套接字分层主要用于通过代理创建安全套接字。HttpClient与实现 SSL/TLS 分层的 `SSLSocketFactory` 一起提供。请注意，HttpClient不使用任何自定义加密功能。它完全依赖于标准Java加密 ( JCE ) 和安全套接字 ( JSEE ) 扩展。



### 2.7.2  与连接管理器集成

定制连接套接字工厂可以与特定协议方案 ( 例如 HTTP 或 HTTPS ) 关联，然后用于创建定制连接管理器。

```java
ConnectionSocketFactory plainsf = <...>
LayeredConnectionSocketFactory sslsf = <...>
Registry<ConnectionSocketFactory> r = RegistryBuilder.<ConnectionSocketFactory>create()
        .register("http", plainsf)
        .register("https", sslsf)
        .build();

HttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(r);
HttpClients.custom().setConnectionManager(cm).build();
```



### 2.7.3  SSL/TLS 定制

HttpClient使用 `SSLConnectionSocketFactory` 创建SSL连接。`SSLConnectionSocketFactory` 允许高度定制。它可以将 `javax.net.ssl.SSLContext` 的实例作为参数，并使用它来创建自定义配置的SSL连接。

```java
KeyStore myTrustStore = <...>
SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(myTrustStore).build();
SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
```

`SSLConnectionSocketFactory` 的定制意味着一定程度上熟悉 SSL/TLS 协议的概念，对此的详细说明不在本文档的范围之内。请参阅[Java™安全套接字扩展 ( JSSE ) 参考指南](http://docs.oracle.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html)，以获取有关 `javax.net.ssl.SSLContext` 和相关工具的详细说明。



### 2.7.4  主机名验证

建立连接后，除了在 SSL/TLS 协议级别执行的信任验证和客户端身份验证外，HttpClient还可选择验证目标主机名是否与服务器X.509证书中存储的名称匹配。该验证可以为服务器信任材料的真实性提供其他保证。`javax.net.ssl.HostnameVerifier` 接口表示用于主机名验证的策略。HttpClient附带了两个 `javax.net.ssl.HostnameVerifier` 实现。重要提示 : 主机名验证不应与SSL信任验证混淆。

* **DefaultHostnameVerifier** : HttpClient使用的默认实现应符合RFC2818。主机名必须与证书指定的任何备用名称匹配，或者在没有给备用名称指定证书主题的最特定CN的情况下。通配符可以出现在CN和任何主题替换中。
* **NoopHostnameVerifier** : 此主机名验证程序实际上关闭了主机名验证。它接受任何有效且与目标主机匹配的SSL会话。

默认情况下，HttpClient使用 `DefaultHostnameVerifier` 实现。如果需要，可以指定其他主机名验证程序实现 :

```java
SSLContext sslContext = SSLContexts.createSystemDefault();
SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
        sslContext,
        NoopHostnameVerifier.INSTANCE);
```

从4.4版开始，HttpClient使用Mozilla Foundation友好维护的公共后缀列表，以确保SSL证书中的通配符不会被滥用以应用于具有公共顶级域的多个域。HttpClient附带了在发行时检索到的列表的副本。列表的最新版本可以在https://publicsuffix.org/list/找到。强烈建议对列表进行本地复制，并每天从其原始位置下载列表不超过一次。

```java
PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.load(
    PublicSuffixMatcher.class.getResource("my-copy-effective_tld_names.dat"));
DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(publicSuffixMatcher);
```

可以使用 `null` 匹配器禁用针对公共后缀列表的验证。

```java
DefaultHostnameVerifier hostnameVerifier = new DefaultHostnameVerifier(null);
```



## 2.8  HttpClient 代理配置

即使HttpClient知道复杂的路由方案和代理链接，它也仅支持开箱即用的简单直接或单次跳跃代理连接。

告诉HttpClient通过代理连接到目标主机的最简单方法是设置默认代理参数 :

```java
HttpHost proxy = new HttpHost("someproxy", 8080);
DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
CloseableHttpClient httpclient = HttpClients.custom().setRoutePlanner(routePlanner).build();
```

还可以指示HttpClient使用标准的JRE代理选择器来获取代理信息 :

```java
SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(
        ProxySelector.getDefault());
CloseableHttpClient httpclient = HttpClients.custom().setRoutePlanner(routePlanner).build();
```

或者，可以提供自定义 `RoutePlanner` 实现，以便对HTTP路由计算过程进行完全控制 :

```java
HttpRoutePlanner routePlanner = new HttpRoutePlanner() {

    public HttpRoute determineRoute(HttpHost target, HttpRequest request, HttpContext context) 
        	throws HttpException {
        return new HttpRoute(target, null,  new HttpHost("someproxy", 8080),
                "https".equalsIgnoreCase(target.getSchemeName()));
    }

};
CloseableHttpClient httpclient = HttpClients.custom().setRoutePlanner(routePlanner).build();
```



# 3  HTTP 状态管理

最初HTTP被设计为无状态的，面向请求/响应的协议，该协议没有为跨越多个逻辑相关的请求/响应交换的有状态会话做出特殊规定。随着HTTP协议的普及和采用，越来越多的系统开始将它用于从未希望的应用程序中，例如作为电子商务应用程序的传输。因此，对状态管理的支持成为必要。

当时，Netscape Communications是Web客户端和服务器软件的领先开发商，它基于专有规范在其产品中实现了对HTTP状态管理的支持。后来，Netscape尝试通过发布规范草案来标准化该机制。这些努力有助于通过RFC标准轨道定义的正式规范。但是，大量应用程序中的状态管理仍主要基于Netscape草案，并且与官方规范不兼容。网络浏览器的所有主要开发人员都被迫保留与那些应用程序的兼容性，这极大地促进了标准遵从性的分散化。



## 3.1  Http cookies

HTTP cookie是HTTP代理和目标服务器可以交换以维护会话的状态信息的令牌或简短数据包。Netscape工程师曾经将其称为"魔术cookie"，这个名字一直沿用至今。

HttpClient使用 `Cookie` 接口来表示抽象Cookie令牌。HTTP cookie以其最简单的形式只是一个名称/值对。通常，HTTP cookie还会包含许多属性，例如有效的域，指定此cookie适用的原始服务器上URL子集的路径以及cookie有效的最长时间。

`SetCookie` 接口表示由源服务器发送到HTTP代理以维护会话状态的 `Set-Cookie` 响应头。

`ClientCookie` 接口通过额外的客户端特定功能扩展了 `Cookie` 接口，例如能够完全按照原始服务器指定的方式检索原始 `Cookie` 属性。 这对于生成 `Cookie` 头很重要，因为某些 `Cookie` 规范要求仅在 `Set-Cookie` 头中指定了 `Cookie` 头时，才应包含某些属性。

这是创建客户端cookie对象的示例 :

```java
BasicClientCookie cookie = new BasicClientCookie("name", "value");
// Set effective domain and path attributes
cookie.setDomain(".mycompany.com");
cookie.setPath("/");
// Set attributes exactly as sent by the server
cookie.setAttribute(ClientCookie.PATH_ATTR, "/");
cookie.setAttribute(ClientCookie.DOMAIN_ATTR, ".mycompany.com");
```



## 3.2  Cookie 规格

`CookieSpec` 接口表示cookie管理规范。cookie管理规范应强制执行 :

* `Set-Cookie` 标头的解析规则。
* 已解析Cookie的验证规则。
* 为给定主机、端口和源路径格式化 `Cookie` 头。

HttpClient附带了几个 `CookieSpec` 实现 :

* **严格标准** : 状态管理策略符合RFC 6265第4节定义的行为规范的语法和语义。
* **标准** : 状态管理策略符合RFC 6265第4节定义的更宽松的配置文件，旨在与不符合良好配置文件的现有服务器进行互操作。
* **Netscape草案 ( 已过时 )** : 此策略符合Netscape Communications发布的原始草案规范。除非绝对需要与遗留代码兼容，否则应避免这样做。
* **RFC 2965 ( 作废 )** : 状态管理策略符合RFC 2965定义的作废状态管理规范。请不要在新应用程序中使用。
* **RFC 2109 ( 作废 )** : 状态管理策略符合RFC 2109定义的作废状态管理规范。请不要在新应用程序中使用。
* **浏览器兼容性 ( 作废 )** : 此策略致力于紧密模仿旧版浏览器应用程序的 ( 错误 ) 行为，例如Microsoft Internet Explorer和Mozilla FireFox。请不要在新的应用程序中使用。
* **默认值** : 默认cookie策略是一种综合策略，它基于随HTTP响应发送的cookie的属性 ( 例如版本属性，现在已过时 )，采用RFC 2965，RFC 2109或Netscape草案兼容的实现。在下一个次要版本的HttpClient中，将不推荐使用此策略，而推荐使用标准 ( 兼容RFC 6265 )。
* **忽略cookie** : 所有cookie被忽略。

强烈建议在新应用程序中使用标准或标准严格策略。过时的规范应仅用于与旧系统兼容。在下一个主要版本的HttpClient中将删除对过时规范的支持。



## 3.3  选择cookie策略

可以在HTTP客户端上设置Cookie策略，如果需要，可以在HTTP请求级别上覆盖它。

```java
RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).build();
CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
RequestConfig localConfig = RequestConfig.copy(globalConfig)
        							   .setCookieSpec(CookieSpecs.STANDARD_STRICT)
        							   .build();
HttpGet httpGet = new HttpGet("/");
httpGet.setConfig(localConfig);
```



## 3.4  自定义cookie策略

为了实现自定义cookie策略，应该创建 `CookieSpec` 接口的自定义实现，创建 `CookieSpecProvider` 实现以创建和初始化自定义规范的实例，并向HttpClient注册工厂。一旦注册了自定义规范，就可以使用与标准Cookie规范相同的方式来激活它。

```java
PublicSuffixMatcher publicSuffixMatcher = PublicSuffixMatcherLoader.getDefault();

Registry<CookieSpecProvider> r = RegistryBuilder.<CookieSpecProvider>create()
        .register(CookieSpecs.DEFAULT,
                new DefaultCookieSpecProvider(publicSuffixMatcher))
        .register(CookieSpecs.STANDARD,
                new RFC6265CookieSpecProvider(publicSuffixMatcher))
        .register("easy", new EasySpecProvider())
        .build();

RequestConfig requestConfig = RequestConfig.custom().setCookieSpec("easy").build();

CloseableHttpClient httpclient = HttpClients.custom()
        								 .setDefaultCookieSpecRegistry(r)
        								 .setDefaultRequestConfig(requestConfig)
        								 .build();
```



## 3.5  Cookie 持久化

HttpClient可以与实现 `CookieStore` 接口的持久性Cookie存储的任何物理表示形式一起使用。默认的 `CookieStore` 实现称为 `BasicCookieStore` 是由 `java.util.ArrayList` 支持的简单实现。当容器对象被垃圾回收时，存储在 `BasicClientCookie` 对象中的Cookie将会丢失。如有必要，用户可以提供更复杂的实现。

```java
// Create a local instance of cookie store
CookieStore cookieStore = new BasicCookieStore();
// Populate cookies if needed
BasicClientCookie cookie = new BasicClientCookie("name", "value");
cookie.setDomain(".mycompany.com");
cookie.setPath("/");
cookieStore.addCookie(cookie);
// Set the store
CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
```



## 3.6  HTTP状态管理和执行上下文

在HTTP请求执行过程中，HttpClient将以下与状态管理相关的对象添加到执行上下文中 :

* `Lookup` 实例代表实际Cookie规范注册表。在本地上下文中设置的此属性的值优先于默认属性。
* `CookieSpec` 实例表示实际cookie规范。
* `CookieOrigin` 实例代表原始服务器的实际详细信息。
* `CookieStore` 实例代表实际的cookie存储。在本地上下文中设置的此属性的值优先于默认属性。

本地 `HttpContext` 对象可用于在执行请求之前自定义HTTP状态管理上下文，或在执行请求后检查其状态。为了实现每个用户 ( 或每个线程 ) 状态管理，还可以使用单独的执行上下文。在本地上下文中定义的Cookie规范注册表和Cookie存储将优先于在HTTP客户端级别设置的默认规范和cookie存储。

```java
CloseableHttpClient httpclient = <...>

Lookup<CookieSpecProvider> cookieSpecReg = <...>
CookieStore cookieStore = <...>

HttpClientContext context = HttpClientContext.create();
context.setCookieSpecRegistry(cookieSpecReg);
context.setCookieStore(cookieStore);
HttpGet httpget = new HttpGet("http://somehost/");
CloseableHttpResponse response1 = httpclient.execute(httpget, context);
<...>
// Cookie origin details
CookieOrigin cookieOrigin = context.getCookieOrigin();
// Cookie spec used
CookieSpec cookieSpec = context.getCookieSpec();
```



# 4  HTTP 认证

HttpClient为HTTP标准规范定义的身份验证方案以及许多广泛使用的非标准身份验证方案 ( 例如 `NTLM` 和 `SPNEGO` ) 提供全面支持。



## 4.1  用户凭证

用户身份验证的任何过程都需要一组可用于建立用户身份的凭据。以最简单的形式，用户凭据可以只是一个用户名/密码对。`UsernamePasswordCredentials` 代表一组凭据，由安全主体和明文密码组成。对于HTTP标准规范定义的标准身份验证方案，此实现就足够了。

```java
UsernamePasswordCredentials creds = new UsernamePasswordCredentials("user", "pwd");
System.out.println(creds.getUserPrincipal().getName());
System.out.println(creds.getPassword());
```

stdout >

```
user
pwd
```

`NTCredentials` 是Microsoft Windows特定的实现，除了用户名/密码对外，还包括一组其他Windows特定属性，例如用户域的名称。在Microsoft Windows网络中，同一用户可以属于多个域，每个域具有一组不同的授权。

```java
NTCredentials creds = new NTCredentials("user", "pwd", "workstation", "domain");
System.out.println(creds.getUserPrincipal().getName());
System.out.println(creds.getPassword());
```

stdout >

```
DOMAIN/user
pwd
```



## 4.2  认证方案

AuthScheme接口表示一个抽象的质询-响应的身份验证方案。认证方案应支持以下功能 :

* 解析并处理目标服务器发送的质询，以响应对受保护资源的请求。
* 提供已处理质询的属性 : 身份验证方案类型及其参数，例如该身份验证方案适用的领域 ( 如果有 )
* 为给定的凭据集和http请求生成授权字符串，以响应实际的授权质询。

请注意，身份验证方案可能是有状态的，涉及一系列的质询-响应交换。

HttpClient附带了多个 `AuthScheme` 实现 :

* **基本** : RFC 2617中定义的基本身份验证方案。此身份验证方案不安全，因为凭据以明文形式传输。尽管不安全，但是如果将基本身份验证方案与 TLS/SSL 加密结合使用，则完全可以满足要求。
* **Digest** : RFC 2617中定义的摘要式身份验证方案。摘要式身份验证方案比Basic安全得多，对于不希望通过 TLS/SSL 加密实现完全传输安全性的那些应用程序来说，它是一个不错的选择。
* **NTLM** : NTLM是由Microsoft开发并针对Windows平台优化的专有身份验证方案。NTLM被认为比Digest更安全。
* **Kerberos** : Kerberos身份验证实现。



## 4.3  凭证提供者

凭据提供程序旨在维护一组用户凭据，并能够为特定的身份验证范围生成用户凭据。认证范围包括主机名、端口号、领域名称和认证方案名称。向凭证提供者注册凭证时，可以提供通配符 ( 任何主机、任何端口、任何领域、任何方案 )，而不提供具体的属性值。如果无法找到直接匹配，则凭证提供者将能够找到特定范围的最接近匹配。

HttpClient可以与实现 `CredentialsProvider` 接口的凭据提供程序的任何物理表示形式一起使用。默认的 `CredentialsProvider` 实现 ( 称为 `BasicCredentialsProvider` ) 是一个由 `java.util.HashMap` 支持的简单实现。

```java
CredentialsProvider credsProvider = new BasicCredentialsProvider();
credsProvider.setCredentials(
    new AuthScope("somehost", AuthScope.ANY_PORT), 
    new UsernamePasswordCredentials("u1", "p1"));
credsProvider.setCredentials(
    new AuthScope("somehost", 8080), 
    new UsernamePasswordCredentials("u2", "p2"));
credsProvider.setCredentials(
    new AuthScope("otherhost", 8080, AuthScope.ANY_REALM, "ntlm"), 
    new UsernamePasswordCredentials("u3", "p3"));

System.out.println(credsProvider.getCredentials(
    new AuthScope("somehost", 80, "realm", "basic")));
System.out.println(credsProvider.getCredentials(
    new AuthScope("somehost", 8080, "realm", "basic")));
System.out.println(credsProvider.getCredentials(
    new AuthScope("otherhost", 8080, "realm", "basic")));
System.out.println(credsProvider.getCredentials(
    new AuthScope("otherhost", 8080, null, "ntlm")));
```

stdout >

```
[principal: u1]
[principal: u2]
null
[principal: u3]
```



## 4.4  HTTP身份验证和执行上下文

HttpClient依赖 `AuthState` 类来跟踪有关身份验证过程状态的详细信息。HttpClient在HTTP请求执行过程中创建了 `AuthState` 的两个实例 : 一个用于目标主机身份验证，另一个用于代理身份验证。如果目标服务器或代理需要用户身份验证，则将在身份验证过程中使用各自的 `AuthScope`、`AuthScheme` 和 `Crednetials` 填充各自的 `AuthScope` 实例。可以检查 `AuthState` 以便发现请求了哪种身份验证，是否找到了匹配的 `AuthScheme` 实现以及凭据提供程序是否设法找到了给定身份验证范围的用户凭据。

在HTTP请求执行过程中，HttpClient将以下与身份验证相关的对象添加到执行上下文中 :

* `Lookup` 实例表示实际身份验证方案注册表。在本地上下文中设置的此属性的值优先于默认属性。
* `CredentialsProvider` 实例代表实际凭证提供者。在本地上下文中设置的此属性的值优先于默认属性。
* `AuthState` 实例代表实际目标身份验证状态。在本地上下文中设置的此属性的值优先于默认属性。
* `AuthCache` 实例代表实际身份验证数据缓存。在本地上下文中设置的此属性的值优先于默认属性。

本地 `HttpContext` 对象可用于在执行请求之前自定义HTTP身份验证上下文，或在执行请求后检查其状态 :

```java
CloseableHttpClient httpclient = <...>

CredentialsProvider credsProvider = <...>
Lookup<AuthSchemeProvider> authRegistry = <...>
AuthCache authCache = <...>

HttpClientContext context = HttpClientContext.create();
context.setCredentialsProvider(credsProvider);
context.setAuthSchemeRegistry(authRegistry);
context.setAuthCache(authCache);
HttpGet httpget = new HttpGet("http://somehost/");
CloseableHttpResponse response1 = httpclient.execute(httpget, context);
<...>

AuthState proxyAuthState = context.getProxyAuthState();
System.out.println("Proxy auth state: " + proxyAuthState.getState());
System.out.println("Proxy auth scheme: " + proxyAuthState.getAuthScheme());
System.out.println("Proxy auth credentials: " + proxyAuthState.getCredentials());
AuthState targetAuthState = context.getTargetAuthState();
System.out.println("Target auth state: " + targetAuthState.getState());
System.out.println("Target auth scheme: " + targetAuthState.getAuthScheme());
System.out.println("Target auth credentials: " + targetAuthState.getCredentials());
```



## 4.5  缓存认证数据

从4.1版开始，HttpClient自动缓存有关已成功通过身份验证的主机的信息。请注意，为了使缓存的身份验证数据从一个请求传播到另一个请求，必须使用相同的执行上下文来执行逻辑上相关的请求。一旦执行上下文超出范围，认证数据将丢失。



## 4.6  抢先认证

HttpClient不支持立即可用的抢占式身份验证，因为抢占式身份验证如果使用不当或使用不当，可能会导致严重的安全问题，例如将用户凭据以明文形式发送给未经授权的第三方。因此，期望用户在其特定应用程序环境中评估先占式身份验证相对于安全风险的潜在好处。

但是，可以通过预填充身份验证数据缓存来将HttpClient配置为抢先进行身份验证。

```java
CloseableHttpClient httpclient = <...>

HttpHost targetHost = new HttpHost("localhost", 80, "http");
CredentialsProvider credsProvider = new BasicCredentialsProvider();
credsProvider.setCredentials(
        new AuthScope(targetHost.getHostName(), targetHost.getPort()),
        new UsernamePasswordCredentials("username", "password"));

// Create AuthCache instance
AuthCache authCache = new BasicAuthCache();
// Generate BASIC scheme object and add it to the local auth cache
BasicScheme basicAuth = new BasicScheme();
authCache.put(targetHost, basicAuth);

// Add AuthCache to the execution context
HttpClientContext context = HttpClientContext.create();
context.setCredentialsProvider(credsProvider);
context.setAuthCache(authCache);

HttpGet httpget = new HttpGet("/");
for (int i = 0; i < 3; i++) {
    CloseableHttpResponse response = httpclient.execute(
            targetHost, httpget, context);
    try {
        HttpEntity entity = response.getEntity();

    } finally {
        response.close();
    }
}
```



## 4.7  NTLM 认证

从4.1版开始，HttpClient提供了对NTLMv1、NTLMv2和NTLM2会话身份验证的全面支持。人们仍然可以继续使用外部NTLM引擎，例如由Samba项目开发的JCIFS库，作为Windows互操作性程序套件的一部分。



### 4.7.1  NTLM连接持久化

就计算开销和性能影响而言，NTLM身份验证方案比标准的基本方案和摘要方案昂贵得多。这可能是Microsoft选择将NTLM身份验证方案设置为有状态的主要原因之一。也就是说，一旦通过身份验证，用户身份将在整个生命周期内与该连接关联。NTLM连接的状态性质使连接持久性更加复杂，因为显而易见的原因，具有不同用户身份的用户可能不会重复使用持久性NTLM连接。HttpClient附带的标准连接管理器完全能够管理状态连接。但是，至关重要的是，同一会话中与逻辑相关的请求使用相同的执行上下文，以使它们知道当前的用户身份。否则，HttpClient最终将针对NTLM保护的资源为每个HTTP请求创建一个新的HTTP连接。有关状态HTTP连接的详细讨论，请参阅本节。

由于NTLM连接是有状态的，因此通常建议使用相对便宜的方法 ( 例如 `GET` 或 `HEAD` ) 触发NTLM身份验证，并重新使用同一连接来执行更昂贵的方法，尤其是那些封装了请求实体的方法，例如 `POST` 或 `PUT` 。

```java
CloseableHttpClient httpclient = <...>

CredentialsProvider credsProvider = new BasicCredentialsProvider();
credsProvider.setCredentials(AuthScope.ANY,
        new NTCredentials("user", "pwd", "myworkstation", "microsoft.com"));

HttpHost target = new HttpHost("www.microsoft.com", 80, "http");

// Make sure the same context is used to execute logically related requests
HttpClientContext context = HttpClientContext.create();
context.setCredentialsProvider(credsProvider);

// Execute a cheap method first. This will trigger NTLM authentication
HttpGet httpget = new HttpGet("/ntlm-protected/info");
CloseableHttpResponse response1 = httpclient.execute(target, httpget, context);
try {
    HttpEntity entity1 = response1.getEntity();
} finally {
    response1.close();
}

// Execute an expensive method next reusing the same context (and connection)
HttpPost httppost = new HttpPost("/ntlm-protected/form");
httppost.setEntity(new StringEntity("lots and lots of data"));
CloseableHttpResponse response2 = httpclient.execute(target, httppost, context);
try {
    HttpEntity entity2 = response2.getEntity();
} finally {
    response2.close();
}
```



## 4.8  SPNEGO / Kerberos身份验证

`SPNEGO` ( 简单且受保护的 `GSSAPI` 协商机制 ) 旨在在双方都不知道对方可以使用/提供什么的情况下允许对服务进行身份验证。它最常用于Kerberos身份验证。它可以包装其他机制，但是HttpClient中的当前版本在设计时仅考虑了Kerberos。



### 4.8.1  HttpClient中的SPNEGO支持

`SPNEGO` 身份验证方案与Sun Java 1.5版及更高版本兼容。但是，强烈建议使用Java> = 1.6，因为它更完整地支持 `SPNEGO` 身份验证。

Sun JRE提供了支持类来执行几乎所有的Kerberos和 `SPNEGO` 令牌处理。这意味着很多设置都是针对GSS类的。  `SPNegoScheme` 是一个简单的类，用于处理封送令牌以及读写正确的标头。

最好的开始方法是在示例中获取 `KerberosHttpClient.java` 文件，然后尝试使其正常工作。可能会发生很多问题，但幸运的是，它可以正常工作而不会出现太多问题。它还应提供一些调试输出。

在Windows中，默认情况下应使用登录的凭据。 这可以通过使用'kinit'来覆盖，例如 `$ JAVA_HOME \ bin \ kinit testuser@AD.EXAMPLE.NET`，对于测试和调试问题非常有用。删除kinit创建的缓存文件，以恢复为Windows Kerberos缓存。

确保在 `krb5.conf` 文件中列出 `domain_realms`。这是问题的主要根源。

> * 客户端Web浏览器对资源执行HTTP GET。
>
> * Web服务器返回HTTP 401状态和响应头 : `WWW-Authenticate: Negotiate`
>
> * 客户端生成 `NegTokenInit`，对其进行base64编码，然后使用Authorization标头重新提交 `GET` : `Authorization: Negotiate <base64 encoding>`。
>
> * 服务器解码 `NegTokenInit`，提取受支持的 `MechType` ( 在我们的情况下仅是Kerberos V5 )，确保它是预期的之一，然后提取 `MechToken` ( Kerberos令牌 ) 并进行身份验证。
>
>   如果需要更多处理，则将另一个HTTP 401返回到客户端，并在 `WWW-Authenticate` 响应头中包含更多数据。客户端获取信息并生成另一个令牌，将此令牌传递回 `Authorization` 响应头中，直到完成。
>
> * 客户端通过身份验证后，Web服务器应返回HTTP 200状态，最终的 `WWW-Authenticate` 响应头和页面内容。



### 4.8.2  GSS / Java Kerberos设置

 本文档假定您使用Windows，但是许多信息也适用于Unix。

`org.ietf.jgss` 类具有许多可能的配置参数，主要在 `krb5.conf / krb5.ini` 文件中。有关格式的更多信息，请参见 http://web.mit.edu/kerberos/krb5-1.4/krb5-1.4.1/doc/krb5-admin/krb5.conf.html。  



### 4.8.3  `login.conf` 文件

以下配置是基本设置，可在Windows XP中针对 `IIS` 和 `JBoss Negotiation` 模块使用。

`login.conf` 内容可能如下所示 :

```java
com.sun.security.jgss.login {
  com.sun.security.auth.module.Krb5LoginModule required client=TRUE useTicketCache=true;
};

com.sun.security.jgss.initiate {
  com.sun.security.auth.module.Krb5LoginModule required client=TRUE useTicketCache=true;
};

com.sun.security.jgss.accept {
  com.sun.security.auth.module.Krb5LoginModule required client=TRUE useTicketCache=true;
};
```



### 4.8.4  `krb5.conf` / `krb5.ini` 文件

 如果未指定，将使用系统默认值。如果需要，可以通过将系统属性 `java.security.krb5.conf` 设置为指向自定义 `krb5.conf` 文件来覆盖。

`krb5.conf` 的内容可能如下所示 :

```
[libdefaults]
    default_realm = AD.EXAMPLE.NET
    udp_preference_limit = 1
[realms]
    AD.EXAMPLE.NET = {
        kdc = KDC.AD.EXAMPLE.NET
    }
[domain_realms]
.ad.example.net=AD.EXAMPLE.NET
ad.example.net=AD.EXAMPLE.NET
```



### 4.8.5  Windows特定配置

 要允许Windows使用当前用户的票证，必须将系统属性 `javax.security.auth.useSubjectCredsOnly` 设置为 `false`，并且应添加Windows注册表项 `allowtgtsessionkey` 并正确设置它以允许在Kerberos Ticket-Granting中发送会话密钥。

在Windows Server 2003和Windows 2000 SP4上，这是必需的注册表设置 :

```
HKEY_LOCAL_MACHINE\System\CurrentControlSet\Control\Lsa\Kerberos\Parameters
Value Name: allowtgtsessionkey
Value Type: REG_DWORD
Value: 0x01
```

这是Windows XP SP2上注册表设置的位置 :

```
HKEY_LOCAL_MACHINE\System\CurrentControlSet\Control\Lsa\Kerberos\
Value Name: allowtgtsessionkey
Value Type: REG_DWORD
Value: 0x01
```



# 5  流式API

## 5.1  易于使用的Facade API

从4.2版本开始，HttpClient附带了一个基于流畅接口概念的易于使用的Facade API。Fluent Facade API仅公开HttpClient的最基本功能，适用于不需要HttpClient完全灵活性的简单用例。例如，流畅的Facade API使用户不必处理连接管理和资源释放。

以下是通过HC Fluent API执行的HTTP请求的几个示例 :

```java
// Execute a GET with timeout settings and return response content as String.
Request.Get("http://somehost/")
        .connectTimeout(1000)
        .socketTimeout(1000)
        .execute().returnContent().asString();
```

```java
// Execute a POST with the 'expect-continue' handshake, using HTTP/1.1,
// containing a request body as String and return response content as byte array.
Request.Post("http://somehost/do-stuff")
        .useExpectContinue()
        .version(HttpVersion.HTTP_1_1)
        .bodyString("Important stuff", ContentType.DEFAULT_TEXT)
        .execute().returnContent().asBytes();
```

```java
// Execute a POST with a custom header through the proxy containing a request body
// as an HTML form and save the result to the file
Request.Post("http://somehost/some-form")
        .addHeader("X-Custom-header", "stuff")
        .viaProxy(new HttpHost("myproxy", 8080))
        .bodyForm(Form.form().add("username", "vip").add("password", "secret").build())
        .execute().saveContent(new File("result.dump"));
```

人们也可以直接使用 `Executor`，以便在特定的安全上下文中执行请求，从而将身份验证详细信息缓存并重新用于后续请求。

```java
Executor executor = Executor.newInstance()
        .auth(new HttpHost("somehost"), "username", "password")
        .auth(new HttpHost("myproxy", 8080), "username", "password")
        .authPreemptive(new HttpHost("myproxy", 8080));

executor.execute(Request.Get("http://somehost/"))
        .returnContent().asString();

executor.execute(Request.Post("http://somehost/do-stuff")
        .useExpectContinue()
        .bodyString("Important stuff", ContentType.DEFAULT_TEXT))
        .returnContent().asString();
```



### 5.1.1  Response 处理

流利的Facade API通常使用户不必处理连接管理和资源重新分配。但是，在大多数情况下，这是以必须在内存中缓冲响应消息的内容为代价的。强烈建议使用 `ResponseHandler` 进行HTTP响应处理，以避免必须在内存中缓冲内容。

```java
Document result = Request.Get("http://somehost/content")
        .execute().handleResponse(new ResponseHandler<Document>() {

    public Document handleResponse(final HttpResponse response) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() >= 300) {
            throw new HttpResponseException(
                    statusLine.getStatusCode(),
                    statusLine.getReasonPhrase());
        }
        if (entity == null) {
            throw new ClientProtocolException("Response contains no content");
        }
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            ContentType contentType = ContentType.getOrDefault(entity);
            if (!contentType.equals(ContentType.APPLICATION_XML)) {
                throw new ClientProtocolException("Unexpected content type:" +
                    contentType);
            }
            String charset = contentType.getCharset();
            if (charset == null) {
                charset = HTTP.DEFAULT_CONTENT_CHARSET;
            }
            return docBuilder.parse(entity.getContent(), charset);
        } catch (ParserConfigurationException ex) {
            throw new IllegalStateException(ex);
        } catch (SAXException ex) {
            throw new ClientProtocolException("Malformed XML document", ex);
        }
    }

    });

```



# 6  Http 缓存

## 6.1  一般概念

HttpClient缓存提供了一个与HTTP / 1.1兼容的缓存层，可与HttpClient一起使用-Java等效于浏览器缓存。该实现遵循责任链设计模式，在该模式中，缓存HttpClient实现可以代替默认的非缓存HttpClient实现; 完全可以从缓存中满足的请求不会导致实际的原始请求。在可能情况下，将使用条件GET和If-Modified-Since and/or If-None-Match请求头，可以自动使用源验证过时的缓存条目。

通常，HTTP / 1.1缓存被设计为在语义上透明; 也就是说，缓存不应更改客户端和服务器之间的请求-响应交换的含义。因此，将缓存的HttpClient放到现有的兼容客户端-服务器关系中应该是安全的。尽管从HTTP协议的角度来看，缓存模块是客户端的一部分，但该实现旨在与透明缓存代理上的要求兼容。

最后，缓存HttpClient包括对RFC 5861指定的Cache-Control扩展的支持 ( 过时错误和过时重新验证 )。

缓存HttpClient执行请求时，它会经历以下流程 :

* 检查请求是否基本符合HTTP 1.1协议，然后尝试更正请求。
* 刷新与该请求相关的任何缓存，使之无效。
* 确定当前请求是否可从缓存中得到满足。如果不是，则在适当时将其缓存后，直接将请求传递给原始服务器并返回响应。
* 如果它是可缓存的请求，它将尝试从缓存中读取它。如果不在高速缓存中，请调用原始服务器并在适当时高速缓存响应。
* 如果缓存的响应适合用作响应，则构造一个包含 `ByteArrayEntity` 的 `BasicHttpResponse` 并将其返回。否则，尝试针对原始服务器重新验证缓存条目。
* 对于无法重新验证的缓存响应，请调用原始服务器并缓存响应 ( 如果适用 )。

当缓存HttpClient收到响应时，它会经历以下流程 :

* 检查响应是否符合协议。
* 确定响应是否可缓存。
* 如果它是可缓存的，则尝试读取配置中允许的最大大小并将其存储在缓存中。
* 如果响应对于高速缓存太大，则重建部分消耗的响应并直接返回而不缓存。

重要的是要注意，缓存HttpClient本身并不是HttpClient的不同实现，但是它通过将自身作为附加处理组件插入到请求执行管道中而起作用。



## 6.2  符合RFC-2616

我们认为HttpClient缓存无条件符合[RFC-2616](http://www.ietf.org/rfc/rfc2616.txt)。就是说，无论规范在哪里表示必须，不得，不应或不应针对HTTP缓存，缓存层都将尝试以满足那些要求的方式运行。这意味着当您放入缓存模块时，它不会产生不正确的行为。



## 6.3  用法示例

这是有关如何设置基本缓存HttpClient的简单示例。按照配置，它将最多存储1000个缓存的对象，每个对象的最大主体大小为8192字节。此处选择的数字仅作为示例，并不具有说明性或建议意义。

```java
CacheConfig cacheConfig = CacheConfig.custom()
								.setMaxCacheEntries(1000)
								.setMaxObjectSize(8192)
								.build();
RequestConfig requestConfig = RequestConfig.custom()
								.setConnectTimeout(30000)
								.setSocketTimeout(30000)
								.build();
CloseableHttpClient cachingClient = CachingHttpClients.custom()
								.setCacheConfig(cacheConfig)
								.setDefaultRequestConfig(requestConfig)
								.build();

HttpCacheContext context = HttpCacheContext.create();
HttpGet httpget = new HttpGet("http://www.mydomain.com/content/");
CloseableHttpResponse response = cachingClient.execute(httpget, context);
try {
    CacheResponseStatus responseStatus = context.getCacheResponseStatus();
    switch (responseStatus) {
        case CACHE_HIT:
            System.out.println("A response was generated from the cache with " +
                    "no requests sent upstream");
            break;
        case CACHE_MODULE_RESPONSE:
            System.out.println("The response was generated directly by the " +
                    "caching module");
            break;
        case CACHE_MISS:
            System.out.println("The response came from an upstream server");
            break;
        case VALIDATED:
            System.out.println("The response was generated from the cache " +
                    "after validating the entry with the origin server");
            break;
    }
} finally {
    response.close();
}
```



## 6.4  配置

缓存HttpClient继承了默认非缓存实现的所有配置选项和参数 ( 包括超时和连接池大小等设置选项 )。对于特定于缓存的配置，可以提供 `CacheConfig` 实例来自定义以下区域的行为 :

1. 缓存大小。如果后端存储支持这些限制，则可以指定最大缓存条目数以及最大可缓存响应正文大小。
2. 公共/私有缓存。默认情况下，缓存模块将其自身视为共享 ( 公共 ) 缓存，例如不会缓存对具有Authorization标头的请求的响应或标记为 `Cache-Control：private` 的响应。但是，如果缓存仅由一个逻辑“用户”使用 ( 类似于浏览器缓存 )，那么您将需要关闭共享缓存设置。
3. 启发式缓存。根据RFC2616，缓存可以缓存某些缓存条目，即使源未设置显式缓存控制头。默认情况下，此行为处于禁用状态，但如果使用的源未设置正确的头，但仍要缓存响应，则可能需要启用此选项。您需要启用启发式缓存，然后指定默认的新鲜度生存期和/或上次修改资源后的一小部分时间。有关启发式缓存的详细信息，请参阅http/1.1rfc的13.2.2和13.2.4节。
4. 后台验证。缓存模块支持RFC5861的stale-while-revalidate指令，该指令允许某些缓存条目重新验证在后台进行。您可能需要调整后台工作线程的最小和最大数量的设置，以及它们在被回收之前可以空闲的最长时间。当没有足够的工作人员满足需求时，您还可以控制用于重新验证的队列的大小。



## 6.5  储存后端

缓存HttpClient的默认实现是将缓存条目和缓存的响应主体存储在应用程序JVM的内存中。尽管这提供了高性能，但由于大小限制或缓存条目是临时性的，并且在应用程序重新启动后无法幸存，因此它可能不适用于您的应用程序。当前版本包括对使用 `EhCache` 和 `memcached` 实现存储缓存条目的支持，这些实现允许将缓存条目溢出到磁盘或将其存储在外部进程中。

如果这些选项都不适合您的应用程序，则可以通过实现 `HttpCacheStorage `接口，然后在构造时将其提供给缓存HttpClient来提供自己的存储后端。在这种情况下，将使用您的方案存储缓存条目，但是您将可以重用围绕HTTP / 1.1遵从性和缓存处理的所有逻辑。一般而言，应该可以从支持键/值存储 ( 类似于Java Map接口 ) 并能够应用原子更新的任何事物中创建 `HttpCacheStorage` 实现。

最后，通过一些额外的努力，完全可以建立多层缓存层次结构。例如，按照类似于虚拟内存，L1 / L2处理器缓存等的模式，将内存中缓存HttpClient包装在磁盘上或远程存储在 `memcached` 中的HttpClient周围。



# 7  进阶主题

## 7.1  自定义客户端连接

在某些情况下，可能有必要自定义HTTP消息通过网络传输的方式，而不是使用HTTP参数所可能实现的方式，以便能够处理非标准，不合规的行为。例如，对于Web爬网程序，可能有必要强制HttpClient接受格式错误的响应头，以挽救消息的内容。

通常，插入自定义消息解析器或自定义连接实现的过程涉及几个步骤 :

* 
