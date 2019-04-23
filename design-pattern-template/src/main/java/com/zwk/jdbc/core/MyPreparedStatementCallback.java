package com.zwk.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 预编译语句回调接口
 * @author zwk
 */
@FunctionalInterface
public interface MyPreparedStatementCallback<T> {

    T doInPreparedStatement(PreparedStatement pstm) throws SQLException;

}
