package com.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class SecurityRestApi {

    @Autowired
    PersonRepository repo;

}
