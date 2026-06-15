package br.com.fabricio.gestao_vagas.modules.candidate.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthCandidateResponseDTO {
    private String access_token;
    private Long expires_in;
}
