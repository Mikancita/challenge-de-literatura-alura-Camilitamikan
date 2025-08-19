package com.example.Literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros (
        @JsonAlias("title") String titulo,
        //@JsonAlias("authors") List<Autor> autores,
        @JsonAlias("authors") List<Autor> autores,
        //@JsonAlias("languages") List<String> idioma,
        @JsonAlias("languages") List<String> idioma,
        @JsonAlias("download_count") Integer Numerodescargas) {
}
