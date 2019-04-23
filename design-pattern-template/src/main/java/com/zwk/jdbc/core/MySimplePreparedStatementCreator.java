package com.zwk.jdbc.core;

import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author zwk
 */
public class MySimplePreparedStatementCreator implements MyPreparedStatementCreator, MySqlProvider {

    private String sql;

    public MySimplePreparedStatementCreator(String sql) {
        Assert.notNull(sql, "sql can not be null");
        this.sql = sql;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
        return conn.prepareStatement(sql);
    }

    @Override
    public String getSql() {
        return this.sql;
    }

}
