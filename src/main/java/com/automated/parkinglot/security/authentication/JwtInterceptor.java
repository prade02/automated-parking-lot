package com.automated.parkinglot.security.authentication;

import com.automated.parkinglot.security.jwt.JwtSecretKeyGenerator;
import com.automated.parkinglot.service.auth.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class JwtInterceptor extends OncePerRequestFilter {

    private static final String ROLE_HIERARCHY_EXPRESSION_DELIMITER = "$$$";
    private final AuthService authService;
    private final RoleHierarchyImpl roleHierarchy;
    private final JwtSecretKeyGenerator jwtSecretKeyGenerator;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String encodedToken = request.getHeader(HttpHeaders.AUTHORIZATION).replace("Bearer ", "");
            SecurityContextHolder.getContext().setAuthentication(parseToken(encodedToken));
        } catch (Exception exception) {
            // log error
        }
        filterChain.doFilter(request, response);
    }

    private Authentication parseToken(String encodedToken) {
        if (!StringUtils.hasText(encodedToken))
            throw new AccessDeniedException("Invalid or No JWT token based");
        Jws<Claims> jwsClaims =
                Jwts.parserBuilder()
                        .setSigningKey(jwtSecretKeyGenerator.getSecretKey())
                        .build()
                        .parseClaimsJws(encodedToken);
        Claims payload = jwsClaims.getBody();
        var authorities = (List<String>) payload.get("authorities");
        Collection<GrantedAuthority> grantedAuthorities =
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
        String username = payload.getSubject();

        Collection<GrantedAuthority> reachableGrantedAuthorities =
                getReachableRoles(grantedAuthorities);

        return new UsernamePasswordAuthenticationToken(username, null, reachableGrantedAuthorities);
    }

    private Collection<GrantedAuthority> getReachableRoles(
            Collection<GrantedAuthority> grantedAuthorities) {
        roleHierarchy.setHierarchy(replaceDelimiter(fetchRoleHierarchyExpression()));
        return roleHierarchy.getReachableGrantedAuthorities(grantedAuthorities);
    }

    private String replaceDelimiter(String expression) {
        return expression.replace(ROLE_HIERARCHY_EXPRESSION_DELIMITER, "\n");
    }

    private String fetchRoleHierarchyExpression() {
        return authService.getRoleHierarchy().getRoleHierarchyExpression();
    }
}
