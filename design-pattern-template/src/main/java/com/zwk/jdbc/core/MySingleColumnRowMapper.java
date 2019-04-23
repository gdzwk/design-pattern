package com.zwk.jdbc.core;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * @author zwk
 */
public class MySingleColumnRowMapper<T> implements MyRowMapper<T> {

    private Class<?> requiredType;

    public MySingleColumnRowMapper(Class<T> requiredType) {
        Assert.notNull(requiredType, "requiredType can not be null.");
        this.requiredType = requiredType;
    }

    @Nullable
    @Override
    public T rowMap(ResultSet rs, int rowNum) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int count = metaData.getColumnCount();
        if (count != 1) {
            throw new RuntimeException("不能返回多列数据");
        }

        Object result = this.getValue(rs, 1, this.requiredType);
        if (result != null && !this.requiredType.isInstance(result)) {
            return null;
        } else {
            return (T) result;
        }
    }

    private Object getValue(ResultSet rs, int index, Class<?> requiredType) throws SQLException {
        if (requiredType == String.class) {
            return rs.getString(index);
        } else if (requiredType == Integer.class || requiredType == int.class) {
            return rs.getInt(index);
        } else if (requiredType == Boolean.class || requiredType == boolean.class) {
            return rs.getBoolean(index);
        } else if (requiredType == Long.class || requiredType == long.class) {
            return rs.getLong(index);
        } else {
            // ....
            return rs.getObject(index);
        }
    }

}
