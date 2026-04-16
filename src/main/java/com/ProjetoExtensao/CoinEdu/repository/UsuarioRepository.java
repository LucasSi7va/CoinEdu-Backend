package com.ProjetoExtensao.CoinEdu.repository;

import com.ProjetoExtensao.CoinEdu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario , Long> {

boolean existsByEmail(String email);
}
