package com.zwk.config.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

/**
 * 嵌入式数据库配置
 * @author zwk
 */
@Configuration
public class EmbeddedDatabaseConfig {

    @Bean
    public DataSource getEmbeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setScriptEncoding("utf-8")
                .addScript("sql/user_data.sql")
                .build();
    }

}
