package com.zwk.jdbc.core;

/**
 * 执行sql提供接口
 * @author zwk
 */
@FunctionalInterface
public interface MySqlProvider {

    /**
     * 获取执行的sql
     */
    String getSql();

}
