package br.com.fabricio.gestao_vagas.modules.candidate;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity(name = "candiadates")
public class CandidateEntity {
    
    @Id
    private UUID id;
    private String name;
    
    @NotBlank
    @Pattern(regexp = "\\S+", message = "Username cannot be blank and not have spaces")
    private String username;
    
    @Email(message = "Email should be valid")
    private String email;
    
    @Length(min = 5, max = 50, message = "Password must have at least 8 characters")
    private String password;
    private String description;
    private String curriculum;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public String getCurriculum() {
        return curriculum;
    }

    public void setCurriculum(String curriculum) {
        this.curriculum = curriculum;
    }


}
