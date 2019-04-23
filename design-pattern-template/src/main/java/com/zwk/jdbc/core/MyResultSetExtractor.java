package com.zwk.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集提取
 * @author zwk
 */
@FunctionalInterface
public interface MyResultSetExtractor<T> {

    /**
     * 提取结果集
     */
    T extractData(ResultSet rs) throws SQLException;

}
