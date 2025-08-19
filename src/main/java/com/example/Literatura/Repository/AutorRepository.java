package com.example.Literatura.Repository;

import com.example.Literatura.model.AutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<AutorEntity, Long> {
    Optional<AutorEntity> findByNombre(String nombre);
}
