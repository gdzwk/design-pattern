package com.zwk.jdbc.fancy;

import com.zwk.jdbc.core.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * @author zwk
 */
@Slf4j
public class FancyJdbcTemplate extends AbstractJdbcTemplate {

    public FancyJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    // #################################################################################################################
    // ################################################ execute statement ##############################################
    // #################################################################################################################
    @Override
    public <T> T execute(MyStatementCallback<T> action) throws DataAccessException {
        Assert.notNull(action, "Callback object can not be null.");
        log.debug("Execute statement :{}", this.getSql(action));

        Connection conn = this.getConnection();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            // 设置语句状态
            this.applyStatementSettings(stmt);
            // 执行语句，获取返回值
            T result = action.doInStatement(stmt);
            // 警告处理
            this.handlerWarnings(stmt);
            return result;
        } catch (SQLException e) {
            log.error("Execute statement error :{}", this.getSql(action), e);
            super.closeStatement(stmt);
            super.closeConnection(conn);
            throw new BadSqlGrammarException("", "", e);
        } finally {
            super.closeStatement(stmt);
            super.closeConnection(conn);
        }
    }

    @Override
    public void execute(String sql) throws DataAccessException {
        Assert.notNull(sql, "sql can not be null.");

        class ExecuteStatementCallback implements MyStatementCallback<Object>, MySqlProvider {
            @Nullable
            @Override
            public Object doInStatement(@NonNull Statement stmt) throws SQLException, DataAccessException {
                stmt.execute(sql);
                return null;
            }

            @Override
            public String getSql() {
                return sql;
            }
        }

        this.execute(new ExecuteStatementCallback());
    }

    // #################################################################################################################
    // ############################################ execute preparedStatement ##########################################
    // #################################################################################################################
    @Override
    public <T> T execute(MyPreparedStatementCreator psc, MyPreparedStatementCallback<T> action) throws DataAccessException {
        Assert.notNull(psc, "MyPreparedStatement can not be null.");
        Assert.notNull(action, "MyPreparedStatementCallback can not be null.");
        log.debug("Execute preparedStatement :{}", this.getSql(action));

        Connection conn = this.getConnection();
        PreparedStatement pstm = null;
        try {
            pstm = psc.createPreparedStatement(conn);
            this.applyStatementSettings(pstm);
            T result = action.doInPreparedStatement(pstm);
            this.handlerWarnings(pstm);
            return result;
        } catch (SQLException e)  {
            log.error("Execute preparedStatement :{}", this.getSql(action));
            this.closeStatement(pstm);
            this.closeConnection(conn);
            throw new BadSqlGrammarException("", "", e);
        } finally {
            this.closeStatement(pstm);
            this.closeConnection(conn);
        }
    }

    @Override
    public <T> T execute(String sql, MyPreparedStatementCallback<T> action) {
        Assert.notNull(sql, "sql can not be null.");
        Assert.notNull(action, "MyPreparedStatementCallback can not be null.");

        return this.execute(new MySimplePreparedStatementCreator(sql), action);
    }

    // #################################################################################################################
    // ############################################## query preparedStatement ##########################################
    // #################################################################################################################
    @Override
    public <T> T query(MyPreparedStatementCreator psc, final MyPreparedStatementSetter pss, final MyResultSetExtractor<T> rse) {
        Assert.notNull(psc, "MyPreparedStatementCreator can not be null.");
        Assert.notNull(rse, "MyResultSetExtractor can not be null.");

        return this.execute(psc, new MyPreparedStatementCallback<T>() {
            @Override
            public T doInPreparedStatement(PreparedStatement pstm) throws SQLException {
                ResultSet rs = null;
                try {
                    if (pss != null) {
                        pss.setValues(pstm);
                    }
                    rs = pstm.executeQuery();
                    return rse.extractData(rs);
                } finally {
                    FancyJdbcTemplate.super.closeResultSet(rs);
                }
            }
        });
    }

    @Override
    public <T> T query(MyPreparedStatementCreator psc, MyResultSetExtractor<T> rse) {
        return this.query(psc, null, rse);
    }

    @Override
    public <T> T query(String sql, MyPreparedStatementSetter pse, MyResultSetExtractor<T> rse) {
        return this.query(new MySimplePreparedStatementCreator(sql), pse, rse);
    }

    @Override
    public <T> T query(String sql, Object[] args, MyResultSetExtractor<T> rse) {
        return this.query(sql, new MyArgumentPreparedStatementSetter(args), rse);
    }

    @Override
    public <T> List<T> query(String sql, Object[] args, MyRowMapper<T> rowMapper) {
        return this.query(sql, new MyArgumentPreparedStatementSetter(args), new MyRowMapperResultSetExtractor<>(rowMapper));
    }

    @Override
    public <T> T queryForObject(String sql, Object[] args, Class<T> returnType) {
        List<T> result = this.query(sql, args, new MySingleColumnRowMapper<>(returnType));
        return DataAccessUtils.nullableSingleResult(result);
    }

    /**
     * 获取连接
     * {@link org.springframework.jdbc.core.JdbcTemplate} 进行了复杂的处理，考虑了包括
     * 事务在内的多种情况，(同一事务下只能够获取一个连接)。这个模板实现类暂时不考虑这个情况
     */
    protected Connection getConnection() {
        try {
            return super.getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("failed to obtain connection.");
        }
    }

    /**
     * 使用前设置
     * @param stmt
     */
    protected void applyStatementSettings(Statement stmt) {
        // ...
    }

    /**
     * 处理sql执行警告
     */
    protected void handlerWarnings(Statement stmt) {
        // ...
    }

    protected String getSql(Object obj) {
        if (obj instanceof MySqlProvider) {
            return ((MySqlProvider) obj).getSql();
        }
        return null;
    }

}
