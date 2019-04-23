package com.zwk.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 预编译语句参数设置
 * @author zwk
 */
public class MyArgumentPreparedStatementSetter implements MyPreparedStatementSetter {

    private Object[] args;

    public MyArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    @Override
    public void setValues(PreparedStatement pstm) throws SQLException {
        if (this.args != null) {
            for (int i = 0; i < this.args.length; i++) {
                this.doSetValues(pstm, i + 1, args[i]);
            }
        }
    }

    private void doSetValues(PreparedStatement pstm, int parameterPosition, Object arg) throws SQLException {
        if (arg instanceof MySqlParameterValue) {
            MySqlParameterValue value = (MySqlParameterValue) arg;
            pstm.setObject(parameterPosition, value.getValue(), value.getSqlType());
        } else {
            pstm.setObject(parameterPosition, arg);
        }
    }

}
