# spring-jdbc 扩展逻辑

主要分为几大类:
* Connection
* Statement
* PreparedStatement
* CallableStatement

以 `Statement`、`PreparedStatement` 为例:

**Statement** :
* T execute(StatementCallback);
  * void execute(String sql);

**PreparedStatement** :
* T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action);
  * T execute(String sql, PreparedStatementCallback<T> action);
  * T query(PreparedStatementCreator psc, PreparedStatementSetter pss, ResultSetExtractor rse);
  * T query(String sql, PreparedStatementSetter pss, ResultSetExtractor rse);
  
  
PreparedStatementSetter:
* ArgumentTypePreparedStatementSetter
* ArgumentPreparedStatementSetter
* PreparedStatementCreatorImpl