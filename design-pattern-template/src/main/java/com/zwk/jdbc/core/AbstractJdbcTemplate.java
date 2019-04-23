package com.zwk.jdbc.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 对 {@link MyJdbcOperations} 的抽象实现，没啥用
 * @author zwk
 */
@Slf4j
public abstract class AbstractJdbcTemplate implements MyJdbcOperations {

    @Getter
    private DataSource dataSource;

    public AbstractJdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void execute(String sql) {
        throw new UnsupportedOperationException("该jdbcTemplate实现不支持此方法");
    }

    @Override
    public <T> T execute(MyStatementCallback<T> stmt) {
        throw new UnsupportedOperationException("该jdbcTemplate实现不支持此方法");
    }

    @Override
    public <T> T execute(MyPreparedStatementCreator psc, MyPreparedStatementCallback<T> action) {
        throw new UnsupportedOperationException("该jdbcTemplate实现不支持此方法");
    }

    @Override
    public <T> T execute(String sql, MyPreparedStatementCallback<T> action) {
        throw new UnsupportedOperationException("该jdbcTemplate实现不支持此方法");
    }

    @Override
    public <T> T query(MyPreparedStatementCreator psc, MyPreparedStatementSetter pss, MyResultSetExtractor<T> rse) {
        throw new UnsupportedOperationException("该jdbcTemplate实现不支持此方法");
    }

    @Override
    public <T> T query(MyPreparedStatementCreator psc, MyResultSetExtractor<T> rse) {
        throw new UnsupportedOperationException("该jdbcTemplate实现不支持此方法");
    }

    @Override
    public <T> T query(String sql, MyPreparedStatementSetter pse, MyResultSetExtractor<T> rse) {
        throw new UnsupportedOperationException("该jdbcTemplate实现不支持此方法");
    }

    @Override
    public <T> T query(String sql, Object[] args, MyResultSetExtractor<T> rse) {
        throw new UnsupportedOperationException("该jdbcTemplate实现不支持此方法");
    }

    @Override
    public <T> List<T> query(String sql, Object[] args, MyRowMapper<T> rowMapper) {
        throw new UnsupportedOperationException("该jdbcTemplate实现不支持此方法");
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, Class<T> returnType) {
        throw new UnsupportedOperationException("该jdbcTemplate实现不支持此方法");
    }

    protected void closeResultSet(ResultSet rs) {
        try {
            if (rs != null && ! rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            log.error("close resultSet error.", e);
        }
    }

    protected void closeStatement(Statement stmt) {
        try {
            if (stmt != null && ! stmt.isClosed()) {
                stmt.close();
            }
        } catch (SQLException e) {
            log.error("close statement error.", e);
        }
    }

    protected void closeConnection(Connection conn) {
        try {
            if (conn != null && ! conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            log.error("close connection error.", e);
        }
    }

}
