package curso.api.rest.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Usuario implements UserDetails {	
	
	@Id
	@GeneratedValue( strategy = GenerationType.AUTO )
	private Long id;
	private String nome;
	private String login;
	private String senha;
	
	@OneToMany( mappedBy = "usuario", orphanRemoval = true, cascade = CascadeType.ALL )
	private List<Telefone> telefones = new ArrayList<Telefone>();
	
	
	
	@OneToMany( fetch = FetchType.EAGER )
	@JoinTable( name = "usuarios_role",  
	
				uniqueConstraints = @UniqueConstraint(
						
					columnNames = {"usuario_id", "role_id"}, 
					name = "unique_rule_user"),
				
					joinColumns = @JoinColumn(
							
							name = "usuario_id", 
					        referencedColumnName = "id", 
					        table = "usuario",
					        unique = false,
					        updatable = false,
					        foreignKey = @ForeignKey(
					        		name = "usuario_fk", 
					        		value = ConstraintMode.CONSTRAINT)
					),
					
					inverseJoinColumns = @JoinColumn(
							
							name = "role_id",
							referencedColumnName = "id",
							table = "role",
							unique = false,
							updatable = false,
							foreignKey = @ForeignKey(
									name = "role_fk",
									value = ConstraintMode.CONSTRAINT)
							
					)
				
				
			)
	private List<Role> roles; //Os papeis ou acessos
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public List<Telefone> getTelefones() {
		return telefones;
	}
	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	/*Autorições, os acessos do usuário: ROLE_ADM, ROLE_VISITANTE etc..*/
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return roles;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.senha;
	}
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.login;
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
	
}
