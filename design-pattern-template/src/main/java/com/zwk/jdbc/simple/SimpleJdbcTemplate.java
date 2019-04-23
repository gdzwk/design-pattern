package com.zwk.jdbc.simple;

import com.zwk.jdbc.core.AbstractJdbcTemplate;
import com.zwk.jdbc.core.MyJdbcOperations;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;

/**
 * 原始jdbc方法的简单模板化处理，只实现了{@link MyJdbcOperations} 中的2个方法。
 *
 * @see MyJdbcOperations#execute(String)
 * @see MyJdbcOperations#queryForObject(String, Object[], Class)
 * @author zwk
 */
@Slf4j
public class SimpleJdbcTemplate extends AbstractJdbcTemplate {

    public SimpleJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void execute(String sql) {
        try (Connection conn = this.getConnection();
             Statement stmt = this.getStatement(conn)) {
            log.debug("Execute statement :{}", sql);
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            // 暂不处理
        }
    }

    @Override
    public <T> T queryForObject(final String sql, Object[] args, Class<T> requiredType) {
        try(Connection conn = this.getConnection();
            PreparedStatement ps = this.getPreparedStatement(conn, sql);
            ResultSet rs = this.getResultSet(ps, args)) {
            log.debug("Execute prepared statement :{}", sql);
            return this.getSingleColumn(rs, 1, requiredType);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析结果集，获取实例集合
     */
    protected  <T> T getSingleColumn(ResultSet rs, int index, Class<T> requiredType) throws SQLException {
        Object result = null;
        int rowIndex = 0;
        if (rs.next()) {
            if (requiredType == String.class) {
                result = rs.getString(index).toString();
            } else if (requiredType == Integer.class || requiredType == int.class) {
                result = rs.getInt(index);
            } else {
                // ...(不做详细扩展了)
                result = rs.getObject(index);
            }
        }
        return (T) result;
    }

    /**
     * 获取sql语句执行的结果集
     */
    protected ResultSet getResultSet(PreparedStatement ps, Object[] values) throws SQLException {
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                ps.setObject(i + 1, values[i]);
            }
        }
        return ps.executeQuery();
    }

    /**
     * 获取预编译sql语句对象
     */
    protected PreparedStatement getPreparedStatement(Connection conn, String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    protected Statement getStatement(Connection conn) throws SQLException {
        return conn.createStatement();
    }

    /**
     * 获取连接
     */
    protected Connection getConnection() throws SQLException {
        return super.getDataSource().getConnection();
    }

}
