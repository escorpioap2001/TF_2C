package es.uv.yeagapalpu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(
		  info =@Info(
		    title = "Acceso a Repositorio de agregados no sql",
		    version = "v1",
		    contact = @Contact(
		      name = "Yerai Aranda y Pablo Albert", email = "yeaga@alumni.uv.es", url = "yeaga@alumni.uv.es"
		    ),
		    license = @License(
		      name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
		    ),
		    description = "Simple aplicacion que accede a la base de datos no sql de nuestros agregados"
		  ),
		  servers = @Server(
		    url = "/",
		    description = "Production"
		  )
		)
public class TrabajoFinalAgregateRepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrabajoFinalAgregateRepoApplication.class, args);
	}

}
