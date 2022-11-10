package com.security.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyPasswordEncoder extends BCryptPasswordEncoder{   
}
