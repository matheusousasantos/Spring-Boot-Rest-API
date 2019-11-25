package curso.api.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController/*Arquitetura REST*/
@RequestMapping(value = "/usuario")
public class IndexController {
	
	/*Serviço RESTfull*/
	@GetMapping( value = "/", produces = "application/json")
	public ResponseEntity init(@RequestParam(value = "nome", required = false) String nome) {
		
		System.out.println("Parâmetro sendo recebido " + nome);
		return new ResponseEntity("Olá Usário REST Spring Boot, Seu nome é " + nome, HttpStatus.OK);
	}
	
}
