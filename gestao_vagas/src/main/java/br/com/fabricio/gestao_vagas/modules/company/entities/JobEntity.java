package br.com.fabricio.gestao_vagas.modules.company.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//after build, need to create a constructor with all args and another with no args, because of the JPA
@Entity(name = "job")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String benefits;
    
    @NotBlank(message = "this field cannot be blank")
    private String level;
    private String description;

    @Column(name = "company_id")
    private UUID companyId;

    @ManyToOne() // many jobs to one company
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private CompanyEntity companyEntity;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
