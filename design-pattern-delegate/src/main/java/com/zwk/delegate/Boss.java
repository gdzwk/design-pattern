package com.zwk.delegate;

/**
 * 老板
 * @author zwk
 */
public class Boss {

    private Leader leader = new Leader();

    public void sendCommand(String command) {
        leader.work(command);
    }

}
