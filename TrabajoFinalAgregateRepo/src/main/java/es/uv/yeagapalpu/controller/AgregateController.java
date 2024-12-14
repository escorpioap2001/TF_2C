package es.uv.yeagapalpu.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.sound.midi.SysexMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import es.uv.yeagapalpu.repository.AggregatedRepository;
import es.uv.yeagapalpu.domain.Ayuntamiento;

@RestController
@RequestMapping("/api/v1")
public class AgregateController {

	@Autowired
	private AggregatedRepository ar;
	
	@GetMapping("agregados")
	@Operation(summary="Obtener agregados", description="Obtener todos los agregados de la base de datos")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Agregados obtenidos", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Ayuntamiento.class)) })})
	public ResponseEntity<?> getAyuntamiento() {
		
		List<Ayuntamiento> agregados;
		agregados = (List<Ayuntamiento>) ar.findAll();
		
		return new ResponseEntity<List<Ayuntamiento>>(agregados, HttpStatus.OK);
	}
	
	@GetMapping("agregado/reciente")
	@Operation(summary="Encontrar agregado más reciente", description="Encuentra el último agregado mediante el timeStamp")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Agregado más reciente encontrado", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Ayuntamiento.class)) })})
	public ResponseEntity<?> getLastAyuntamiento() {
		List<Ayuntamiento> a = ar.findAllByOrderByTimeStamp();
		if(a.size() > 0) {
			return new ResponseEntity<Ayuntamiento>(a.get(a.size() - 1), HttpStatus.OK); 
		}
		return new ResponseEntity<String>("No hay ningún dato aún", HttpStatus.OK); 
	}
	
	@PostMapping("agregado")
	@Operation(summary="Crear agregado", description="Crea un nuevo agregado a partir de un parking y la estación más próxima")
	@ApiResponses(value = { 
		    @ApiResponse(responseCode = "200", description = "Agregado creado", 
		    content = { @Content(mediaType = "application/json", 
		      schema = @Schema(implementation = Ayuntamiento.class)) }),
		    @ApiResponse(responseCode = "400", description = "Agregado enviado no válido", 
		    content = @Content)})
	public ResponseEntity<?> createAyuntamiento(@RequestBody Ayuntamiento ayuntamiento) {
		ayuntamiento.setTimeStamp(LocalDateTime.now());
		Ayuntamiento a = ar.insert(ayuntamiento);
		if(a != null) {
			return new ResponseEntity<Ayuntamiento>(a, HttpStatus.CREATED); 
		}
		return new ResponseEntity<Ayuntamiento>(new Ayuntamiento(), HttpStatus.BAD_REQUEST);
	}
}