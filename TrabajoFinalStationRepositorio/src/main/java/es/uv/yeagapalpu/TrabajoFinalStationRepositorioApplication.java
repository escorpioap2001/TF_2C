package es.uv.yeagapalpu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;


@SpringBootApplication
@OpenAPIDefinition(
		  info =@Info(
		    title = "Acceso a Repositorio de estaciones",
		    version = "v1",
		    contact = @Contact(
		      name = "Yerai Aranda y Pablo Albert", email = "yeaga@alumni.uv.es", url = "yeaga@alumni.uv.es"
		    ),
		    license = @License(
		      name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
		    ),
		    description = "Simple aplicacion que accede a la base de datos de nuestras estaciones"
		  ),
		  servers = @Server(
		    url = "/",
		    description = "Production"
		  )
		)
@EnableJpaRepositories("es.uv.yeagapalpu.repository")
@EntityScan("es.uv.yeagapalpu.domain")
public class TrabajoFinalStationRepositorioApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrabajoFinalStationRepositorioApplication.class, args);
	}

}
