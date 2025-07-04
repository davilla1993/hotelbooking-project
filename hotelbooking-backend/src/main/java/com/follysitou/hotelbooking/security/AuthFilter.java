package com.follysitou.hotelbooking.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = getTokenFromRequest(request);

        try {
            if (token != null) {
                String email = jwtUtils.getUsernameFromToken(token); // ← peut lancer ExpiredJwtException
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

                if (StringUtils.hasText(email) && jwtUtils.isTokenValid(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }

            // Si tout va bien, on laisse passer la requête
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            log.warn("Token expiré : {}", ex.getMessage());

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String json = new ObjectMapper().writeValueAsString(Map.of(
                    "status", 401,
                    "error", "Unauthorized",
                    "message", "Votre session a expiré. Veuillez vous reconnecter.",
                    "timestamp", LocalDateTime.now().toString()
            ));

            response.getWriter().write(json);

            // Ne pas continuer la requête
            return;

        } catch (Exception e) {
            log.error("Erreur pendant le filtrage : {}", e.getMessage());

            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String json = new ObjectMapper().writeValueAsString(Map.of(
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", "Erreur serveur",
                    "timestamp", LocalDateTime.now().toString()
            ));

            response.getWriter().write(json);

            // Ne pas continuer non plus
            return;
        }
    }


    private String getTokenFromRequest(HttpServletRequest request){
        String tokenWithBearer = request.getHeader("Authorization");
        if (tokenWithBearer != null && tokenWithBearer.startsWith("Bearer ")) {
            return tokenWithBearer.substring(7);
        }
        return null;
    }
}
