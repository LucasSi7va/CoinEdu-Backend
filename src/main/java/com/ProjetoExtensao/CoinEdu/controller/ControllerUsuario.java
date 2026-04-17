package com.ProjetoExtensao.CoinEdu.controller;

import com.ProjetoExtensao.CoinEdu.dto.UsuarioCarteiraDTO;
import com.ProjetoExtensao.CoinEdu.dto.UsuarioDto;
import com.ProjetoExtensao.CoinEdu.model.Usuario;
import com.ProjetoExtensao.CoinEdu.service.ServiceCarteira;
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

@Autowired
private final ServiceCarteira serviceCarteira;

@GetMapping("{id}")
    public ResponseEntity<UsuarioDto> getUsuario(@PathVariable Long id){
    return  serviceUsuario.getIdUsuario(id);
}


@PostMapping("/cadastrar")
    public ResponseEntity<UsuarioDto> CadastrarUsuario(@RequestBody Usuario usuario){
    return serviceUsuario.cadastrarUsuario(usuario);
}

@GetMapping("/login")
public ResponseEntity<UsuarioCarteiraDTO> login(@RequestParam String email){
    return serviceUsuario.acessarLogin(email);
}



@PostMapping("/carteira/favoritar")
public ResponseEntity<String> favoritar(
        @RequestParam Long usuarioId,
        @RequestParam String moeda
) {
    serviceCarteira.salvarCarteira(usuarioId, moeda);

    return ResponseEntity.ok("Moeda adicionada com sucesso");
}

}
