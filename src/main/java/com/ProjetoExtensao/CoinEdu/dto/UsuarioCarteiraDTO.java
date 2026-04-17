package com.ProjetoExtensao.CoinEdu.dto;

import java.util.List;

public record UsuarioCarteiraDTO(
        String nome,
        String email,
        String senha ,
        List<MoedaDto> moeda
) {
}
