package com.zwk.serialize;

import java.io.*;

/**
 * 自定义克隆接口，通过序列化/反序列化实现深复制
 * @author zwk
 */
public interface DeepCloneable extends Serializable {

    /**
     * 通过序列化/反序列化实现深复制
     * @return 深复制的新对象
     */
    default Object deepClone() {
        Object obj = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);

            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
