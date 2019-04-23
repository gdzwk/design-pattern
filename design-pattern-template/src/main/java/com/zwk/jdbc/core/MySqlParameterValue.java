package com.zwk.jdbc.core;

import lombok.Getter;

/**
 * @author zwk
 */
public class MySqlParameterValue extends MySqlParameter {

    @Getter
    private Object value;

    public MySqlParameterValue(int sqlType, Object value) {
        super(sqlType);
        this.value = value;
    }

}
