package com.zwk.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zwk
 */
@RestController
@RequestMapping("/user")
public class JdbcController {

    @Autowired
    JdbcService JdbcService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public void getUserById(@PathVariable("id") Long id) {
        JdbcService.get(id);
    }

}
