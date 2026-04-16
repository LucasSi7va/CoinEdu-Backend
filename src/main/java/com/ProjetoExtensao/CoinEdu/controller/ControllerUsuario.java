package com.ProjetoExtensao.CoinEdu.controller;

import com.ProjetoExtensao.CoinEdu.dto.UsuarioDto;
import com.ProjetoExtensao.CoinEdu.model.Usuario;
import com.ProjetoExtensao.CoinEdu.service.ServiceUsuario;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@AllArgsConstructor
public class ControllerUsuario {

@Autowired
private final ServiceUsuario serviceUsuario;


@GetMapping("{id}")
    public ResponseEntity<UsuarioDto> getUsuario(@PathVariable Long id){
    return  serviceUsuario.getIdUsuario(id);
}

@PostMapping("/cadastrar")
    public ResponseEntity<UsuarioDto> CadastrarUsuario(@RequestBody Usuario usuario){
    return serviceUsuario.cadastrarUsuario(usuario);
}

}
