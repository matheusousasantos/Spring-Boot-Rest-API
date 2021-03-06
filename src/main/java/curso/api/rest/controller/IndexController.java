package curso.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;


@CrossOrigin( origins = "https://192.168.101" )
@RestController
@RequestMapping(value = "/usuario")
public class IndexController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping( value = "/{id}/codigovenda/{venda}", produces = "application/json")
	public ResponseEntity<Usuario> relatorio(@PathVariable(value = "id") Long id,
			@PathVariable(value = "venda") Long venda) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);	
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	/*Serviço RESTfull*/
	@GetMapping( 
			value = "/{id}", 
			produces = "application/json", 
			headers = "X-API-Version=v1")
	public ResponseEntity<Usuario> initV1(@PathVariable(value = "id") Long id) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);	
		
		System.out.println("Executando versão 1 ");
		
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	/*Serviço RESTfull*/
	@GetMapping( 
			value = "{id}", 
			produces = "application/json",
			headers = "X-API-Version=v2")
	public ResponseEntity<Usuario> initV2(@PathVariable(value = "id") Long id) {
		
		Optional<Usuario> usuario = usuarioRepository.findById(id);	
		
		System.out.println("Executando versão 2 ");
		
		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces="application/text")
	public String delete( @PathVariable("id") Long id ) {
		
		usuarioRepository.deleteById(id);
		
		return "OK!"; //Retorna apenas um OK
	}
	
	@CrossOrigin( origins = "*" )
	@GetMapping( value="/", produces = "application/json")
	@Cacheable( "cacheusuarios" )
	public ResponseEntity<List<Usuario>> usuario() throws InterruptedException {
		
		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();
		
//      Segura o código por 6 segundos simulando o código lento
		Thread.sleep(6000); 
		
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);
	}
	
	/* POST */
	@PostMapping(value = "/", produces ="application/json")
	public ResponseEntity<Usuario> cadastrar( @RequestBody Usuario usuario ) {
		
//      Amarrando esses telefones a esse usuário
		for( int pos = 0; pos < usuario.getTelefones().size(); pos++ ) {
			usuario.getTelefones().get(pos).setUsuario(usuario);;
		}
		
//	    Criptografando senha
		String senhaCryptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhaCryptografada);
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
	/* POST */
	@PostMapping(value = "/{iduser}/idvenda/{idvenda}", produces ="application/json")
	public ResponseEntity cadastrarvenda( @PathVariable Long iduser, 
			                                       @PathVariable Long idvenda) {
		return new ResponseEntity("id user: " + iduser + " idvenda: " + idvenda , HttpStatus.OK);
	}
	
	/* PUT */
	@PutMapping(value = "/", produces ="application/json")
	public ResponseEntity<Usuario> atualizar( @RequestBody Usuario usuario ) {
		
//      Amarrando esses telefones a esse usuário
		for( int pos = 0; pos < usuario.getTelefones().size(); pos++ ) {
			usuario.getTelefones().get(pos).setUsuario(usuario);
		}
		
		Usuario userTemporario = usuarioRepository.
				findUserByLogin(usuario.getLogin());
		
//		Senhas doferentes
		if(!userTemporario.getSenha().equals(usuario.getSenha())) {
			
			String senhaCryptografada = new BCryptPasswordEncoder()
					.encode(usuario.getSenha());
			
			usuario.setSenha(senhaCryptografada);

		}
		
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}
	
}
