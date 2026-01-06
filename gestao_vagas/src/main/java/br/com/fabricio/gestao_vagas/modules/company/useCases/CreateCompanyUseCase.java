package br.com.fabricio.gestao_vagas.modules.company.useCases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fabricio.gestao_vagas.exceptions.UserFoundException;
import br.com.fabricio.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.fabricio.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class CreateCompanyUseCase {

    @Autowired
    private CompanyRepository companyRepository;
    public void execute(CompanyEntity companyEntity) {

        this.companyRepository.findByUsernameOrEmail(companyEntity.getUsername(), companyEntity.getEmail()).ifPresent((user) -> { //case user is already present
            throw new UserFoundException();
        });
        
        this.companyRepository.save(companyEntity);
    }
    
}
