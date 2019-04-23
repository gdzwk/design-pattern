package com.zwk.pool;

import lombok.Data;

/**
 * 连接对象
 * @author zwk
 */
@Data
public class Connection {

    private int id;

    public Connection(int id) {
        this.id = id;
    }

}
