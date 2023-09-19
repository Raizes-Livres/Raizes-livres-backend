package com.generation.raizeslivres.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.raizeslivres.Models.Login;
import com.generation.raizeslivres.Models.Usuario;
import com.generation.raizeslivres.Repository.UsuarioRepository;
import com.generation.raizeslivres.Security.JwtService;

@Service
public class LoginService{


   @Autowired
   private UsuarioRepository usuarioRepository;


   @Autowired
   private JwtService jwtService;


   @Autowired
   private AuthenticationManager authenticationManager;


   public Optional<Usuario> cadastrarUsuario(Usuario usuario) {


       if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()){
           return Optional.empty();
       }
       usuario.setSenha(criptografarSenha(usuario.getSenha()));
       return Optional.of(usuarioRepository.save(usuario));
   }


   public Optional<Usuario> atualizarUsuario(Usuario usuario) {


       if (usuarioRepository.findById(usuario.getId()).isPresent()) {
           Optional<Usuario> buscaUsuario = usuarioRepository.findByEmail(usuario.getEmail());


           if ((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId())) {
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
           }


           usuario.setSenha(criptografarSenha(usuario.getSenha()));


           return Optional.of(usuarioRepository.save(usuario));


       }


       return Optional.empty();


   }


   public Optional<Login> autenticarUsuario(Optional<Login> usuarioLogin) {
       var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(), usuarioLogin.get().getSenha());


       Authentication authentication = authenticationManager.authenticate(credenciais);


       if (authentication.isAuthenticated()) {

           Optional<Usuario> usuario = usuarioRepository.findByEmail(usuarioLogin.get().getUsuario());

           if (usuario.isPresent()) {

               usuarioLogin.get().setId(usuario.get().getId());
               usuarioLogin.get().setNome(usuario.get().getNome());
               usuarioLogin.get().setFoto(usuario.get().getFoto());
               usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario()));
               usuarioLogin.get().setSenha("");

               return usuarioLogin;
           }
       }
       return Optional.empty();
   }


   private String criptografarSenha(String senha) {
       BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
       return encoder.encode(senha);
   }


   private String gerarToken(String usuario) {
       return "Bearer " + jwtService.generateToken(usuario);
   }
}

