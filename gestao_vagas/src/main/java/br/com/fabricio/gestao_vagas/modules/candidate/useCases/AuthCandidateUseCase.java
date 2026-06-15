package br.com.fabricio.gestao_vagas.modules.candidate.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.fabricio.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.fabricio.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.fabricio.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;

import org.springframework.beans.factory.annotation.Value;

@Service
public class AuthCandidateUseCase {
    //verify if my candidate exists in database and if the password is correct, if it is correct return a token
    @Value("${security.token.secret.candidate}")
    private String secretKey;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO AuthCandidateRequestDTO) throws AuthenticationException
    {
        var candidate = this.candidateRepository.findByUsername(AuthCandidateRequestDTO.username())
        .orElseThrow(() -> {
            throw new UsernameNotFoundException("username/password incorrect");
        });

        var passwordMatch = this.passwordEncoder.matches(AuthCandidateRequestDTO.password(), candidate.getPassword());

        if (!passwordMatch)
        {
            throw new AuthenticationException();
        }
        //--
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofMinutes(10));
        var token = JWT.create().withIssuer("javagas")
            .withSubject(candidate.getId().toString())
            .withClaim("roles", Arrays.asList("candidate"))
            .withExpiresAt(expiresIn)
            .sign(algorithm);
        
        var authCandidateResponse = AuthCandidateResponseDTO.builder()
            .access_token(token)
            .expires_in(expiresIn.toEpochMilli())
            .build();
        
        return authCandidateResponse;
    }
}
