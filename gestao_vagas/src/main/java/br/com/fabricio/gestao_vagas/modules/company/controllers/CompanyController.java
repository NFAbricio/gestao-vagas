package br.com.fabricio.gestao_vagas.modules.company.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fabricio.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.fabricio.gestao_vagas.modules.company.useCases.CreateCompanyUseCase;

@RestController
@RequestMapping("/company")
public class CompanyController {
    
    @Autowired
    private CreateCompanyUseCase createCompanyUseCase;

    @RequestMapping("/")
    public ResponseEntity<Object> create(@RequestBody CompanyEntity companyEntity){
        try{
            this.createCompanyUseCase.execute(companyEntity);
            return ResponseEntity.ok().body(companyEntity);
        } catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
