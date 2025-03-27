package com.jga.les.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jga.les.dtos.LoginDto;
import com.jga.les.model.Usuario;
import com.jga.les.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService{
    private Algorithm algorithm = Algorithm.HMAC256("q5!&9dze8uJ5VWT%uk*M&!q8");
    private String issuer = "jga-les-api";

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) usuarioRepository.findByLogin(username);
    }

    public String getToken(LoginDto loginDto){
         Usuario usuario = usuarioRepository.findByLogin(loginDto.login());
        return generateToken(usuario);
    }

    public String generateToken(Usuario usuario){
        try {
            String token = JWT.create()
                        .withIssuer(this.issuer)
                        .withSubject(usuario.getLogin())
                        .withExpiresAt(geraDataExpiracao())
                        .sign(this.algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token: " + exception.getMessage());
        }
    }

    public String validateToken(String token){
        try {
            return JWT.require(this.algorithm)
                .withIssuer(this.issuer)
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    } 

    private Instant geraDataExpiracao() {
        return LocalDateTime.now().plusHours(10).toInstant(ZoneOffset.of("-03:00"));
    }
}
