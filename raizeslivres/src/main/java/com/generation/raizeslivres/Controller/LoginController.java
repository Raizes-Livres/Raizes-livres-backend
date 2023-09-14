package com.generation.raizeslivres.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.raizeslivres.Models.Login;
import com.generation.raizeslivres.Models.Usuario;
import com.generation.raizeslivres.Repository.UsuarioRepository;
import com.generation.raizeslivres.Service.LoginService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LoginController {


   @Autowired
   private LoginService loginService;


   @Autowired
   private UsuarioRepository usuarioRepository;


   @GetMapping("/all")
   public ResponseEntity <List<Usuario>> getAll(){
       return ResponseEntity.ok(usuarioRepository.findAll());
   }


   @GetMapping("/{id}")
   public ResponseEntity<Usuario> getById(@PathVariable Long id) {
       return usuarioRepository.findById(id)
               .map(resposta -> ResponseEntity.ok(resposta))
               .orElse(ResponseEntity.notFound().build());
   }


   @PostMapping("/logar")
   public ResponseEntity<Login> autenticarUsuario(@RequestBody Optional<Login> login){
       return loginService.autenticarUsuario(login)
               .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
               .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
   }




   @PostMapping("/cadastrar")
   public ResponseEntity<Usuario> postUsuario(@RequestBody @Valid Usuario usuario) {
       return loginService.cadastrarUsuario(usuario)
               .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
               .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
   }


   @PutMapping("/atualizar")
   public ResponseEntity<Usuario> putUsuario(@Valid @RequestBody Usuario usuario) {
       return loginService.atualizarUsuario(usuario)
               .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
               .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
   }


}

