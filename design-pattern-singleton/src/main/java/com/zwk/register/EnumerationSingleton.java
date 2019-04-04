package com.zwk.register;

/**
 * 枚举单例
 * @author zwk
 */
public enum EnumerationSingleton {

    SINGLETON;

    private Object content;

    public static EnumerationSingleton getInstance() {
        return SINGLETON;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

}
