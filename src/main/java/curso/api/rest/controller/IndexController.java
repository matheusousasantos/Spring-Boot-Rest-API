package curso.api.rest.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import curso.api.rest.model.Usuario;

@RestController/*Arquitetura REST*/
@RequestMapping(value = "/usuario")
public class IndexController {
	
	/*Serviço RESTfull*/
	@GetMapping( value = "/", produces = "application/json")
	public ResponseEntity<Usuario> init() {
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Matheus Sousa");
		usuario.setLogin("123");
		usuario.setSenha("123");
		
		Usuario usuario1 = new Usuario();
		usuario1.setId(2L);
		usuario1.setNome("Luma Hashilley");
		usuario1.setLogin("555");
		usuario1.setSenha("555");
		
		List<Usuario> usuarios = new ArrayList<Usuario>();
		
		usuarios.add(usuario);
		usuarios.add(usuario1);
		
		return new ResponseEntity(usuarios, HttpStatus.OK);
	}
	
}
