package com.zwk.delegate;

/**
 * 雇员B
 * @author zwk
 */
public class EmployeeB implements IEmployee {

    @Override
    public void work(String command) {
        System.out.println("处理业务代码");
    }

}
