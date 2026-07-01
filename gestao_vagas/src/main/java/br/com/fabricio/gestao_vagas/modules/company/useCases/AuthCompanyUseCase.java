package br.com.fabricio.gestao_vagas.modules.company.useCases;

import br.com.fabricio.gestao_vagas.modules.company.controllers.AuthCompanyController;

import java.lang.reflect.Array;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.fabricio.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.fabricio.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import br.com.fabricio.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class AuthCompanyUseCase {
    
    @Value("${security.token.secret}")
    private String secretKey;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
        var company = this.companyRepository.findByUsername(authCompanyDTO.getUsername()).orElseThrow(
            () -> {
                throw new UsernameNotFoundException("username/password incorrect");
            }
        );

        var passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());

        if(!passwordMatches)
        {
            throw new AuthenticationException();
        }

        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        var expires = Instant.now().plus(Duration.ofHours(2));

        var token = JWT.create().withIssuer("javagas")
            .withExpiresAt(expires)
            .withSubject(company.getId().toString())
            .withClaim("roles", Arrays.asList("COMPANY"))
            .sign(algorithm);

            var authCompanyResponseDTO = AuthCompanyResponseDTO.builder()
            .access_token(token)
            .expires_in(expires.toEpochMilli())
            .build();
            
        return authCompanyResponseDTO;
    }

}
