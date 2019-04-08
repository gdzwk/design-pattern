package com.zwk.proxy.staticproxy;

import com.zwk.proxy.IDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

/**
 * dao静态代理类
 * @author zwk
 */
@Getter
@Setter
public class StaticDaoProxy implements IDao {

    private IDao dao;

    @Override
    public void save() {
        Assert.notNull(dao, "请填充代理对象");

        this.beforeProcessing();
        dao.save();
        this.afterProcessing();
    }

    private void beforeProcessing() {
        System.out.println("前置动作");
    }

    private void afterProcessing() {
        System.out.println("后置动作");
    }

}
