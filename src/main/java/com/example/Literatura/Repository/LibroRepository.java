package com.example.Literatura.Repository;

import com.example.Literatura.model.LibroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibroRepository extends JpaRepository<LibroEntity, Long> {
    Optional<LibroEntity> findByTituloContainsIgnoreCase(String titulo);
}

