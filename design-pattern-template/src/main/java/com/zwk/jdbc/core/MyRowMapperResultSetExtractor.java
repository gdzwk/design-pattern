package com.zwk.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zwk
 */
public class MyRowMapperResultSetExtractor<T> implements MyResultSetExtractor<List<T>> {

    private MyRowMapper<T> myRowMapper;

    /**
     * 用于初始化集合
     */
    private int rowsExpected;

    public MyRowMapperResultSetExtractor(MyRowMapper<T> myRowMapper) {
        this(myRowMapper, 0);
    }

    public MyRowMapperResultSetExtractor(MyRowMapper<T> myRowMapper, int rowsExpected) {
        this.myRowMapper = myRowMapper;
        this.rowsExpected = rowsExpected;
    }

    @Override
    public List<T> extractData(ResultSet rs) throws SQLException {
        List<T> result = (this.rowsExpected > 0) ? new ArrayList<>(rowsExpected) : new ArrayList<>();
        int rowNum = 1;
        while (rs.next()) {
            result.add(this.myRowMapper.rowMap(rs, rowNum++));
        }
        return result;
    }

}
