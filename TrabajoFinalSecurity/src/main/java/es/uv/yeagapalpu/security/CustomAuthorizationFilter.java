package es.uv.yeagapalpu.security;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.uv.yeagapalpu.service.JwtService;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

	private JwtService jwtService;
	
	public CustomAuthorizationFilter(JwtService jwtService) {
		this.jwtService = jwtService;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		if(request.getServletPath().equals("/api/v1/login")) {
			filterChain.doFilter(request, response);
		}
		else {		
			String authHeader = request.getHeader(AUTHORIZATION);
			if(authHeader != null && authHeader.startsWith("Bearer ")) {
				try {
					String token = jwtService.getTokenFromHeader(authHeader);
					String username = jwtService.getUsernameFromToken(token);		
					
					Collection<SimpleGrantedAuthority> authorities = jwtService.getAuthoritiesFromToken(token);
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
				}
				catch(Exception exception) {
					response.setHeader("error", exception.getMessage());
					response.setStatus(403);
					System.out.println(403);
					Map<String, String> error = new HashMap<>();
					error.put("error_msg", exception.getMessage());
					response.setContentType(MediaType.APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(), error);
				}
			}
			else {
				filterChain.doFilter(request, response);
			}
		}
	}

	
	
}
