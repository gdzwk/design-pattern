package com.zwk.proxy.customdynamic;

import lombok.NonNull;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.charset.Charset;

/**
 * 自定义动态代理类
 * @author zwk
 */
public class CustomDynamicProxy {

    /**
     * 包路径
     */
    private static final String PACKAGE_NAME = CustomDynamicProxy.class.getPackage().getName();

    /**
     * 文件路径
     */
    private static final String FILE_PATH = CustomDynamicProxy.class.getResource("").getPath();

    /**
     * 代理类类名
     */
    private static final String PROXY_CLASS_NAME = "$proxy1";

    /**
     * 换行符
     */
    private static final String LINE_BREAK = "\r\n";

    /**
     * 获取自定义代理类实例
     * @param classLoader  类加载器
     * @param _interface  实现接口
     * @param <T>  返回类型
     */
    @SuppressWarnings("unchecked")
    public static <T> T newProxyInstance(@NonNull ClassLoader classLoader, @NonNull Class<T> _interface, @NonNull CustomInvocationHandler handler) {
        T obj = null;
        try {
            Class<?> clazz = compilerJavaFile(classLoader, _interface);
            obj = (T) clazz.getConstructor(InvocationHandler.class).newInstance(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 获取动态生成的java代理类
     * @param _interface  实现接口
     * @return 代理类
     */
    private static Class<?> compilerJavaFile(ClassLoader classLoader, Class<?> _interface) {
        try {
            File javaFile = getJavaFileInfo(_interface);
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, Charset.forName("UTF-8"));
            JavaCompiler.CompilationTask task = compiler.getTask(null, manager, null, null, null, manager.getJavaFileObjects(javaFile));
            task.call();
            manager.close();

            Class<?> clazz = classLoader.loadClass(PACKAGE_NAME + "." + PROXY_CLASS_NAME);

            javaFile.delete();
            return clazz;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("加载失败");
        }
    }

    /**
     * 动态生成java代理类文本信息
     * @param _interface  实现接口
     */
    private static File getJavaFileInfo(Class<?> _interface) {
        Method[] methods = _interface.getMethods();

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("package %s;%s", PACKAGE_NAME, LINE_BREAK));
        sb.append(String.format("import java.lang.reflect.*;%s", LINE_BREAK));
        sb.append(String.format("import %s;%s", _interface.getName(), LINE_BREAK));
        sb.append(String.format("public class %s implements %s {%s", PROXY_CLASS_NAME, _interface.getSimpleName(), LINE_BREAK));
        // 定义接口方法
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            sb.append(String.format("   private static Method m%s;%s", i, LINE_BREAK));
        }
        sb.append(String.format("   private InvocationHandler h;%s", LINE_BREAK));
        sb.append(String.format("   public %s(InvocationHandler h) {%s", PROXY_CLASS_NAME, LINE_BREAK));
        sb.append(String.format("       this.h = h;%s", LINE_BREAK));
        sb.append(String.format("   }%s", LINE_BREAK));
        // 方法调用
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            sb.append(String.format("   public final %s %s() {%s", m.getReturnType().getSimpleName(), m.getName(), LINE_BREAK));
            sb.append(String.format("       try {%s", LINE_BREAK));
            if (m.getReturnType() == Void.TYPE) {
                sb.append(String.format("           this.h.invoke(this, m%s, (Object[])null);%s", i, LINE_BREAK));
            } else {
                sb.append(String.format("           return (%s) this.h.invoke(this, m%s, (Object[])null);%s", m.getReturnType().getSimpleName(), i, LINE_BREAK));
            }
            sb.append(String.format("       } catch (RuntimeException | Error var2) {%s", LINE_BREAK));
            sb.append(String.format("           throw var2;%s", LINE_BREAK));
            sb.append(String.format("       } catch (Throwable var3) {%s", LINE_BREAK));
            sb.append(String.format("           throw new UndeclaredThrowableException(var3);%s", LINE_BREAK));
            sb.append(String.format("       }%s", LINE_BREAK));
            sb.append(String.format("   }%s", LINE_BREAK));
        }
        // 获取接口方法
        sb.append(String.format("   static {%s", LINE_BREAK));
        sb.append(String.format("       try {%s", LINE_BREAK));
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            sb.append(String.format("           m%s = Class.forName(\"%s\").getMethod(\"%s\");%s", i, _interface.getName(), m.getName(), LINE_BREAK));
        }
        sb.append(String.format("       } catch (Exception e) {%s", LINE_BREAK));
        sb.append(String.format("           e.printStackTrace();%s", LINE_BREAK));
        sb.append(String.format("           throw new RuntimeException(\"加载失败\");%s", LINE_BREAK));
        sb.append(String.format("       }%s", LINE_BREAK));
        sb.append(String.format("   }%s", LINE_BREAK));
        sb.append(String.format("}%s", LINE_BREAK));

        File file = new File(FILE_PATH + "/" + PROXY_CLASS_NAME + ".java");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(sb.toString().getBytes());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件不存在");
        }
        return file;
    }

}
