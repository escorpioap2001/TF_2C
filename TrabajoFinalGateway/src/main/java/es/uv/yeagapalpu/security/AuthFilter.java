package es.uv.yeagapalpu.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.common.net.HttpHeaders;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
	
	@Autowired
	private WebClient.Builder client;
	
	@Value("${auth.url}")
	private String auth_url;
	
	@Value("#{'${auth.excluded-routes}'.split(',')}")
    private List<String> excludedRoutes;
	
	private AntPathMatcher pathMatcher = new AntPathMatcher();
	
	public AuthFilter() {
        super(Config.class);
    }
	
	@Override
	public GatewayFilter apply(Config config) {
		
		return (exchange, chain) -> {
			
			ServerHttpResponse response = exchange.getResponse();
            String requestPath = exchange.getRequest().getURI().getPath();

            // Check if the request path matches any excluded route
            for (String excludedRoute : excludedRoutes) {
                if (pathMatcher.match(excludedRoute, requestPath)) {
                    return chain.filter(exchange);  // Skip authentication for excluded routes
                }
            }
			
			if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				return response.setComplete();
			}
			
			String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			if(!authHeader.contains("Bearer")) {
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				return response.setComplete();
			}
						
			         
			try {
				return client.build()
					         .get()
					         .uri(auth_url+"/auth/authorize")
					         .header(HttpHeaders.AUTHORIZATION, authHeader)
					         .retrieve()
					         .onStatus(httpStatus -> httpStatus.value() != HttpStatus.ACCEPTED.value(),
					        	 error -> { return Mono.error(new Throwable("UNAUTHORIZED")); })
					         .toEntity(String.class)
					         .flatMap(entity -> {	    	 
					       	     if(entity.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
					    	 	     response.setStatusCode(HttpStatus.UNAUTHORIZED);
					    	 	     return response.setComplete();
					    	     }	
					       	     exchange.getRequest()
					       	     		 .mutate()
					       	     		 .header("x-auth-user-id", entity.getBody());
					    	     return chain.filter(exchange);
					         });
			}
			catch (Throwable e){
				response.setStatusCode(HttpStatus.UNAUTHORIZED);
				return response.setComplete();
			}
		};

	}
	
	public static class Config{ }
	
}
