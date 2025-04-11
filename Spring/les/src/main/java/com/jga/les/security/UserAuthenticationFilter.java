package com.jga.les.security;

import com.jga.les.model.UserDetailsImpl;
import com.jga.les.model.Usuario;
import com.jga.les.repository.UsuarioRepository;
import com.jga.les.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.jga.les.security.SecurityConfiguration.ENDPOINTS_PUBLICOS;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Se o endpoint for público, não faz nada
        if (isEndpointPublico(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extrai o token do cabeçalho
        String token = recoverToken(request);
        if (token == null) {
            throw new RuntimeException("Token não encontrado.");
        }

        // Valida o token e obtém o login do usuário
        String login = jwtTokenService.getSubjectFromToken(token);
        Usuario usuario = usuarioRepository.findByLogin(login).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Converte as permissões do usuário em autoridades (ex: "USUARIO_ADD", "PRODUTO_DELETE")
        UserDetails userDetails = new UserDetailsImpl(usuario);

        // Cria a autenticação e define no contexto
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }

    private boolean isEndpointPublico(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        // Remove o contextPath da URI (ex: se a aplicação está em "/api", remove "/api" da URI)
        String path = uri.substring(contextPath.length());

        // Verifica se o path corresponde a algum endpoint público
        return Arrays.stream(ENDPOINTS_PUBLICOS)
                .anyMatch(publicPath -> new AntPathMatcher().match(publicPath, path));
    }
}