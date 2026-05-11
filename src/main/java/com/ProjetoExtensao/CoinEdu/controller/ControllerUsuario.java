package com.ProjetoExtensao.CoinEdu.controller;

import com.ProjetoExtensao.CoinEdu.dto.*;
import com.ProjetoExtensao.CoinEdu.dto.filtroGlobal.FiltroGlobal;
import com.ProjetoExtensao.CoinEdu.model.Usuario;
import com.ProjetoExtensao.CoinEdu.service.ServiceCarteira;
import com.ProjetoExtensao.CoinEdu.service.ServiceUsuario;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

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
    public ResponseEntity<CadastroResponseDto> CadastrarUsuario(@RequestBody Usuario usuario , HttpServletRequest request){
    return serviceUsuario.cadastrarUsuario(usuario , request.getRemoteAddr());
}

@PostMapping("/confirmar-cadastro")
public ResponseEntity<UsuarioDto> confirmarCadastro(
        @RequestBody ConfirmarCadastroDto dto) {
return serviceUsuario.confirmarCadastro(dto.email() , dto.codigo());
}

@GetMapping("/login")
public ResponseEntity<UsuarioCarteiraDTO> login(FiltroGlobal filtroGlobal){
    return serviceUsuario.acessarLogin(filtroGlobal);
}


@PostMapping("/carteira/favoritar")
public ResponseEntity<String> favoritar(
        @RequestParam Long usuarioId,
        @RequestParam String moeda
) {
    serviceCarteira.salvarCarteira(usuarioId, moeda);

    return ResponseEntity.ok("Moeda adicionada com sucesso");
}


    @DeleteMapping("/carteira/remover")
    public ResponseEntity<String> removerCarteira(
            @RequestParam Long usuarioId,
            @RequestParam String moeda
    ) {
        serviceCarteira.removerCarteira(usuarioId, moeda);

        return ResponseEntity.ok("Moeda Removida com sucesso");
    }

    @GetMapping("/carteira/simulacao")
    public ResponseEntity<SimulacaoDto> simularCompra(
            @RequestParam String moeda,
            @RequestParam BigDecimal valorCompra,
            @RequestParam Long usuarioId) {

        return serviceCarteira.simularCompra(usuarioId, moeda, valorCompra);
    }


    @PatchMapping("/{id}/alternar-modo-idoso")
    public ResponseEntity<ModoIdosoDto> ModoIdoso(@PathVariable Long id) {
        return serviceUsuario.modoIdoso(id);
    }

}
