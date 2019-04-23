package com.zwk.jdbc.core;

import lombok.Data;
import lombok.Setter;
import org.springframework.lang.Nullable;

/**
 * sql参数定义
 * @author zwk
 */
@Data
public class MySqlParameter {

    @Nullable
    private String name;

    /**
     * @see java.sql.Types
     */
    private int sqlType;

    private String sqlTypeName;

    private Integer scale;

    public MySqlParameter(int sqlType) {
        this.sqlType = sqlType;
    }

}
