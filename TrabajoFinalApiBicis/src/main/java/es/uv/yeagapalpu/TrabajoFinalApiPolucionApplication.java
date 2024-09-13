package es.uv.yeagapalpu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(
		  info =@Info(
		    title = "Api para el control de parkings de bicicletas",
		    version = "v1",
		    contact = @Contact(
		      name = "Yerai Aranda y Pablo Albert", email = "yeaga@alumni.uv.es", url = "yeaga@alumni.uv.es"
		    ),
		    license = @License(
		      name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
		    ),
		    description = "Api que nos permite gestionar parkings de bicibletas"
		  ),
		  servers = @Server(
		    url = "/",
		    description = "Production"
		  )
		)
public class TrabajoFinalApiPolucionApplication {
	
	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(TrabajoFinalApiPolucionApplication.class, args);
	}

}
