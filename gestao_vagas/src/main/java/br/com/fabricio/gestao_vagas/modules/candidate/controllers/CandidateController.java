package br.com.fabricio.gestao_vagas.modules.candidate.controllers;

import br.com.fabricio.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fabricio.gestao_vagas.exceptions.UserFoundException;
import br.com.fabricio.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.fabricio.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.fabricio.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/candidate")
public class CandidateController {

    private final ProfileCandidateUseCase profileCandidateUseCase;

    @Autowired
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private ProfileCandidateUseCase profiileCandidateUseCase;

    CandidateController(ProfileCandidateUseCase profileCandidateUseCase) {
        this.profileCandidateUseCase = profileCandidateUseCase;
    }

    @PostMapping("/")
//     public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidateEntity) {
//        try{
//             var result = this.createCandidateUseCase.execute(candidateEntity);
//             return ResponseEntity.ok().body(result);
//        }catch(Exception e){
//             return ResponseEntity.badRequest().body(e.getMessage());
//        }
//     }
     public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidateEntity) throws Exception {
          var result = this.createCandidateUseCase.execute(candidateEntity);
          return ResponseEntity.ok().body(result);
     }

     @GetMapping("/")
     public ResponseEntity<Object> get(HttpServletRequest request)
     {

          var idCandidate = request.getAttribute("candidate_id");
          try
          {
                this.profileCandidateUseCase.execute(UUID.fromString(idCandidate.toString()));
                return ResponseEntity.ok().body(null);
          }
          catch (Exception e)
          {
               return ResponseEntity.badRequest().body(e.getMessage());
          }
     }
}
