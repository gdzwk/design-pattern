package com.zwk.jdbc.core;

import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;

/**
 * 用于指定一组基本的 JDBC 操作
 * @see JdbcOperations
 * @author zwk
 */
public interface MyJdbcOperations {

    // --------------------------------------execute statement-------------------------------------
    void execute(String sql);

    <T> T execute(MyStatementCallback<T> stmt);

    // ----------------------------------execute preparedStatement---------------------------------
    <T> T execute(MyPreparedStatementCreator psc, MyPreparedStatementCallback<T> action);

    <T> T execute(String sql, MyPreparedStatementCallback<T> action);

    // ----------------------------------- query preparedStatement --------------------------------
    // ----------------------------- 进一步拆分callback (setter/extract) ----------------------------
    <T> T query(MyPreparedStatementCreator psc, MyPreparedStatementSetter pss, MyResultSetExtractor<T> rse);

    <T> T query(MyPreparedStatementCreator psc, MyResultSetExtractor<T> rse);

    <T> T query(String sql, MyPreparedStatementSetter pse, MyResultSetExtractor<T> rse);

    <T> T query(String sql, Object[] args, MyResultSetExtractor<T> rse);

    <T> List<T> query(String sql, Object[] args, MyRowMapper<T> rowMapper);

    // ----------------------------------- query preparedStatement --------------------------------
    // ------------------------------------- 进一步细分到查找单个对象 ---------------------------------
    <T> T queryForObject(final String sql, Object[] args, Class<T> returnType);

}
