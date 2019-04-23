package com.zwk.jdbc.core;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zwk
 */
@FunctionalInterface
public interface MyRowMapper<T> {

    /**
     * 将结果集中指定行转化为指定的对象
     */
    @Nullable
    T rowMap(@NonNull ResultSet rs, @NonNull int rowNum) throws SQLException;

}
