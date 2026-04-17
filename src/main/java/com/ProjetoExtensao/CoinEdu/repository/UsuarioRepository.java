package com.ProjetoExtensao.CoinEdu.repository;

import com.ProjetoExtensao.CoinEdu.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario , Long> {

    boolean existsByEmail(String email);


    @Query("SELECT u FROM Usuario u JOIN FETCH u.carteira WHERE u.email = :email")
    Optional<Usuario> buscarPorCompleto(@Param("email") String email);
}