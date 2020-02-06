package curso.api.rest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import curso.api.rest.ApplicationContextLoad;
import curso.api.rest.model.Usuario;
import curso.api.rest.repository.UsuarioRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {
	
//	Tempo de validade do Token
	private static final long EXPIRATION_TIME = 17800000;
	
//	Uma senha unica para compor a autenticação
	private static final String SECRET = "SenhaExtremamenteSecreta";
	
//	Prefixo padrão de Token
	private static final String TOKEN_PREFIX = "Bearer";
	
//	Prefixo retornado ao cabeçalho pra identificar o token/resposta
	private static final String HEADER_STRING = "Authorization";

//	Gerando Token de autenticado e adicionando ao cabeçalho a resposta HTTP
	public void addAuthentication( 
			HttpServletResponse response,  
			String username) throws IOException {
//		recebe a resposta do http assim como o username
		
//		Montagem do Token - precisamos inserir uma dependência
		String JWT = Jwts.builder()
		.setSubject(username) //Seta o usuário
		.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME /*Data mais o tempo de expiração */)) //Tempo que ele vai demorar pra expirar
		.signWith(SignatureAlgorithm.HS512, SECRET).compact(); //Compactação e algoritmos de geração de senha
	
//	    Precisamos juntar o JWT com o prefixo pra formarmos o Token
//		O token fica assim: 'Bearer 4445444nn4445n4545n4545'
		String token = TOKEN_PREFIX + " " + JWT;
		
//		Adiciona no cabeçalho http | token
//		Respota fica assim: 'Authorization: Bearer 4445444nn4445n4545n4545'
		response.addHeader(HEADER_STRING, token);
		
//		Escreve token como resposta no corpo do http
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
		System.out.println("{\"Authorization\": \"" + token + "\"}");
		
	}
	
//  Esse método recebe a requisição do navegador
//	Retorna o usuário 'validado' com token caso não seja válido retorna null
	public Authentication getAuthentication( HttpServletRequest request) {
		
//		pega o token enviado no cabeçalho http
		String token = request.getHeader(HEADER_STRING);
		
//		Verifica de o token existe
		if(token != null) {
			
//			Faz a validação do token do usuário na requisição
			String user = Jwts.parser()
					.setSigningKey(SECRET)
					
//                   Vamos pegar o token e tirar o prefixo(Bearer) dele
//                   Vamos tirar o TOKEN_PREFIX e inserir uma String vazia
//                   Irá ficar:'4445444nn4445n4545n4545'
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
					.getBody().getSubject(); //Irá me retornar somente o usuário
			
			if(user != null) { //Ser o usuário realmente existir
				
//				ApplicationContext - Todos os controllers, daos.. tudo que foi carregado na memória
				Usuario usuario = (Usuario) ApplicationContextLoad.getApplicationContext()
						.getBean(UsuarioRepository.class).findUserByLogin(user);
				
//				Retornar o usuário logado
				if(usuario != null) {
					
//					Retorna o usuário
					return new UsernamePasswordAuthenticationToken(
							usuario.getLogin(), 
							usuario.getSenha(),
							usuario.getAuthorities());
				}
				
			} 
			
		}
		
		return null;
	}

}
