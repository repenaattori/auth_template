package com.security.auth;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class SecurityService {
    
    @Autowired
    PersonRepository repo;

    @Autowired
    MyPasswordEncoder myEncoder;

    @Value("${jwt.secret}")
    private String jwtKey;

    @PostConstruct
    void init(){
        repo.save(new User("repe", myEncoder.encode("repe")));
    }

    /**
     * Register new user or update existing one
     * @param uname
     * @param pw
     * @return
     */
    public User register(String uname, String pw){
       
        User u = new User(uname, myEncoder.encode(pw));
        repo.save(u);
        return u;
    }

    /**
     * Login user. Return token or null if not found or wrong password.
     * @param uname
     * @param pw
     * @return
     */
    public String login(String uname, String pw){
     
        User u = repo.findById(uname).orElse(null);

        if(u == null || !myEncoder.matches(pw, u.password) ){
            return null;
        }

        Algorithm alg = Algorithm.HMAC256(jwtKey);
        return JWT.create().withSubject(u.username).sign(alg);
    }

    /**
     * Verify jwt token and return username if token is valid
     * @param jwtToken
     * @return
     */
    public String validateJwt(String jwtToken){
        Algorithm alg = Algorithm.HMAC256(jwtKey);
        JWTVerifier verifier = JWT.require(alg).build();

        try {
            DecodedJWT jwt = verifier.verify(jwtToken);
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
        }

        return null;
    }

    /**
     * This contains some cors configurations. Try adding this if the @CrossOrigin in the rest api doesn't work
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH",
                "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type",
                "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new
                UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
