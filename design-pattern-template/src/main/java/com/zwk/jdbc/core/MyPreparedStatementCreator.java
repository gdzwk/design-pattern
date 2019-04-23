package com.zwk.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * {@link PreparedStatement} 有多种创建方式
 * @author zwk
 */
@FunctionalInterface
public interface MyPreparedStatementCreator {

    /**
     * 创建 {@link PreparedStatement} 实例
     */
    PreparedStatement createPreparedStatement(Connection conn) throws SQLException;

}
