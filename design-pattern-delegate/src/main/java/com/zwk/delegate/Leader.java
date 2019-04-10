package com.zwk.delegate;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目经理
 * @author zwk
 */
public class Leader implements IEmployee {

    private Map<String, IEmployee> targets = new HashMap<>(16);

    public Leader() {
        targets.put("底层架构", new EmployeeA());
        targets.put("业务处理", new EmployeeB());
    }

    @Override
    public void work(String command) {
        targets.get(command).work(command);
    }

}
