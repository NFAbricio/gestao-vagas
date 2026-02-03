package br.com.fabricio.gestao_vagas.modules.company.useCases;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.fabricio.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.fabricio.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class AuthCompanyUseCase {
    
    @Value("${security.token.secret}")
    private String secretKey;
    
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public String execute(AuthCompanyDTO authCompanyDto) throws AuthenticationException {
        var company = this.companyRepository.findByUsername(authCompanyDto.getUsername()).orElseThrow(
            () -> {
                throw new UsernameNotFoundException("User not found");
            });

        //verify if password matches
        var passwordMatches = this.passwordEncoder.matches(authCompanyDto.getPassword(), company.getPassword());
        //if not, throw exception
        if (!passwordMatches) {
            throw new AuthenticationException();
        }

        //if matches, generate Token
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        var token =JWT.create().withIssuer("javagas")
        .withSubject(company.getId().toString())
        .sign(algorithm);

        return token;
    }

}
