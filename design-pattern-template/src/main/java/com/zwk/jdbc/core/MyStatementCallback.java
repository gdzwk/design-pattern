package com.zwk.jdbc.core;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * 语句回调接口
 * @author zwk
 * @param <T>
 */
@FunctionalInterface
public interface MyStatementCallback<T> {

    @Nullable
    T doInStatement(@NonNull Statement stmt) throws SQLException;

}
