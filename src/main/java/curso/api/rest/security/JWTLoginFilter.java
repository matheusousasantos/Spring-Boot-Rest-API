package curso.api.rest.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

import curso.api.rest.model.Usuario;

/*Configurando o nosso gerenciador de Token*/
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {


	protected JWTLoginFilter(String url, AuthenticationManager authenticationManager) {

//    * Chama o contrutor principal da classe extendida.
//    * Vai autenticar a nossa URL
//    * Obrigamos a autenticar  a URL
		super(new AntPathRequestMatcher(url));
		
//      Gerenciador de auenticação
		setAuthenticationManager(authenticationManager);
	}

//  Retorna o usuário ao processar a autenticação
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		
//      Está pegando o Token para validar
		Usuario user = new ObjectMapper().
				
//              Ler o fluxo de dados vindo da requisição
				readValue(request.getInputStream(), Usuario.class);
		
//      retorna o usuário login, senha e acessos
		return getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getSenha()));
	}
	
// Se der tudo certo... Vamos usar o serviço criado anteriormente
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		new JWTTokenAutenticacaoService().addAuthentication(response, authResult.getName());
		
	}

}
