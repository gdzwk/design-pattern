package com.zwk.demo;

import com.zwk.config.datasource.User;
import com.zwk.jdbc.core.MyJdbcOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zwk
 */
@Service
public class JdbcService {

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    MyJdbcOperations myJdbcOperations;

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void get(Long id) {
        String name = myJdbcOperations.queryForObject("select name from user where id = ?", new Object[] {id}, String.class);
        System.out.println(name);
    }

}
