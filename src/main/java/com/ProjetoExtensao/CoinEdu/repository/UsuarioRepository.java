package com.ProjetoExtensao.CoinEdu.repository;

import com.ProjetoExtensao.CoinEdu.dto.UsuarioDto;
import com.ProjetoExtensao.CoinEdu.dto.filtroGlobal.FiltroGlobal;
import com.ProjetoExtensao.CoinEdu.model.Usuario;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario , Long> {

    boolean existsByEmail(String email);

    Optional<Usuario>  findByEmail(String email);

    @Query("""
    SELECT u FROM Usuario u JOIN FETCH u.carteira WHERE 
    (:email IS NULL OR u.email ILIKE :email) AND
    (:nome IS NULL OR u.nome ILIKE :nome)
""")
    Optional<Usuario> buscarPorCompleto(
            @Param("email") String email,
            @Param("nome") String nome
    );
}