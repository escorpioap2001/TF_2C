package es.uv.yeagapalpu.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import es.uv.yeagapalpu.service.JwtService;
import es.uv.yeagapalpu.domain.AuthenticationRequest;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;


@RestController
@RequestMapping("/auth")
public class AuthorizationController {

	@Autowired
	private PasswordEncoder pe;
	
	@Autowired
	private JwtService tp;
	
	@GetMapping("authorize")
	@Operation(summary="Servicio para validar el token del usuario", description="Servicio para validar el token del usuario")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Token validado", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = String.class)) }),
		    @ApiResponse(responseCode = "401", description = "No estás autorizado", 
		    content = @Content) })
	public Mono<ResponseEntity<?>> validate(ServerWebExchange exchange){
		String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		String token = tp.getTokenFromHeader(header);
		try {
			if(!tp.isTokenExpired(tp.getTokenFromHeader(header))) {
				return Mono.just(ResponseEntity.status(HttpStatus.ACCEPTED).body(tp.getUsernameFromToken(token)));
			}
			else {
				return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token"));
			}
		}
		catch(Exception e){
			return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token"));
		}	
	}
	
	@PostMapping("authenticate")
	@Operation(summary="Servicio para obtener el token del usuario", description="Servicio para obtener el token del usuario")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Indentifiación satisfactoria", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = String.class)) }),
		    @ApiResponse(responseCode = "401", description = "Identificación incorrecta", 
		    content = @Content) })
    public Mono<ResponseEntity<?>> login(@RequestBody AuthenticationRequest auth) {
		
		Mono<String> mono = Mono.just(auth.getUsername());
		return mono.map(user -> {
			if (user.startsWith("S")) {
				Map<String, String> tokens = new HashMap<>();
				String accessToken = this.tp.generateAccessToken(user, Arrays.asList("ESTACION"));
				String refreshToken = this.tp.generateRefreshToken(user, Arrays.asList("ESTACION"));
				tokens.put("access_token", accessToken);
				tokens.put("refresh_token", refreshToken);
				HttpHeaders headers = new HttpHeaders();
			    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
				return ResponseEntity.ok()
									 .headers(headers)
									 .body(tokens);
			}
			else if (user.startsWith("P")) {
				Map<String, String> tokens = new HashMap<>();
				String accessToken = this.tp.generateAccessToken(user, Arrays.asList("PARKING"));
				String refreshToken = this.tp.generateRefreshToken(user, Arrays.asList("PARKING"));
				tokens.put("access_token", accessToken);
				tokens.put("refresh_token", refreshToken);
				HttpHeaders headers = new HttpHeaders();
			    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
				return ResponseEntity.ok()
									 .headers(headers)
									 .body(tokens);
			}
			else if (user.equals("ADMIN")) {
				Map<String, String> tokens = new HashMap<>();
				String accessToken = this.tp.generateAccessToken(user, Arrays.asList("ADMIN"));
				String refreshToken = this.tp.generateRefreshToken(user, Arrays.asList("ADMIN"));
				tokens.put("access_token", accessToken);
				tokens.put("refresh_token", refreshToken);
				HttpHeaders headers = new HttpHeaders();
			    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
				return ResponseEntity.ok()
									 .headers(headers)
									 .body(tokens);
			}
			else if (user.equals("SERVICIO")) {
				Map<String, String> tokens = new HashMap<>();
				String accessToken = this.tp.generateAccessToken(user, Arrays.asList("SERVICIO"));
				String refreshToken = this.tp.generateRefreshToken(user, Arrays.asList("SERVICIO"));
				tokens.put("access_token", accessToken);
				tokens.put("refresh_token", refreshToken);
				HttpHeaders headers = new HttpHeaders();
			    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
				return ResponseEntity.ok()
									 .headers(headers)
									 .body(tokens);
			} 
			else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
	
	@PostMapping("refresh")
	@Operation(summary="Servicio para refrescar el token del usuario", description="Servicio para refrescar el token del usuario")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Token refrescado satisfactoriamente", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = String.class)) }),
		    @ApiResponse(responseCode = "401", description = "Identificación incorrecta", 
		    content = @Content) })
    public Mono<ResponseEntity<?>> refresh(ServerWebExchange exchange) {
		String header = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		try {
			String refreshToken = tp.getTokenFromHeader(header);
			if(!tp.isTokenExpired(tp.getTokenFromHeader(header))) {
				String accessToken = this.tp.generateAccessToken(tp.getUsernameFromToken(refreshToken), Arrays.asList(tp.getRolesFromToken(refreshToken)));
				HttpHeaders headers = new HttpHeaders();
			    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
			    headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
			    Map<String, String> tokens = new HashMap<>();
			    tokens.put("access_token", accessToken);
				tokens.put("refresh_token", refreshToken);
				return Mono.just(ResponseEntity.ok()
						                       .headers(headers)
						                       .body(tokens));
			}
			else {
				return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token"));
			}
		}
		catch(Exception e){
			return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token"));
		}
	}
	
}
