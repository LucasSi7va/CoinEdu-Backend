package com.ProjetoExtensao.CoinEdu.service;


import com.ProjetoExtensao.CoinEdu.dto.UsuarioDto;
import com.ProjetoExtensao.CoinEdu.model.Usuario;
import com.ProjetoExtensao.CoinEdu.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ServiceUsuario {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public ResponseEntity<UsuarioDto> getIdUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Id nao encontrado"));

        return ResponseEntity.ok(new UsuarioDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha())
        );
    }


    public ResponseEntity<UsuarioDto> cadastrarUsuario(Usuario usuario) {

        usuario.setNome(usuario.getNome().trim());

        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Email ja cadastrado");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(
                new UsuarioDto(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getSenha())
        );



    }
}
