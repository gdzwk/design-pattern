package com.zwk.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 预编译语句参数设置
 * @author zwk
 */
@FunctionalInterface
public interface MyPreparedStatementSetter {

    /**
     * 设置参数
     */
    void setValues(PreparedStatement pstm) throws SQLException;

}
