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
		    title = "Api para tratamiento de datos del ayuntamiento",
		    version = "v1",
		    contact = @Contact(
		      name = "Yerai Aranda y Pablo Albert", email = "yeaga@alumni.uv.es", url = "yeaga@alumni.uv.es"
		    ),
		    license = @License(
		      name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
		    ),
		    description = "Aplicación que nos permite recolecar la información de diferentes servicios"
		  ),
		  servers = @Server(
		    url = "/",
		    description = "Production"
		  )
		)
public class TrabajoFinalApiAyuntamientoApplication {
	
	@LoadBalanced
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(TrabajoFinalApiAyuntamientoApplication.class, args);
	}

}
