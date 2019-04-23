package com.zwk.config.auto;

import com.zwk.jdbc.core.MyJdbcOperations;
import com.zwk.jdbc.fancy.FancyJdbcTemplate;
import com.zwk.jdbc.simple.SimpleJdbcTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author zwk
 */
@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class JdbcAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "custom.jdbcTemplate", havingValue = "simple")
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public MyJdbcOperations getSimpleJdbcTemplate(DataSource dataSource) {
        return new SimpleJdbcTemplate(dataSource);
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "custom.jdbcTemplate", havingValue = "fancy")
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public MyJdbcOperations getFancyJdbcTemplate(DataSource dataSource) {
        return new FancyJdbcTemplate(dataSource);
    }

}
