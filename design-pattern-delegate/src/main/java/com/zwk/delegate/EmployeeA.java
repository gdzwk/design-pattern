package com.zwk.delegate;

/**
 * 雇员A
 * @author zwk
 */
public class EmployeeA implements IEmployee {

    @Override
    public void work(String command) {
        System.out.println("设计底层架构");
    }

}
