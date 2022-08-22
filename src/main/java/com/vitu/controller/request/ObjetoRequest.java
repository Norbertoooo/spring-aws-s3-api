package com.vitu.controller.request;

import javax.validation.constraints.NotBlank;

public record ObjetoRequest(@NotBlank String bucketNomeOrigem,@NotBlank String objetoNome,
                            @NotBlank String bucketNomeDestino, String objetoNomeDestino) {
}
