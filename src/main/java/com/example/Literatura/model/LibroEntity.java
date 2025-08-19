package com.example.Literatura.model;

import com.example.Literatura.model.AutorEntity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="libros")
public class LibroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String autor;
//    @Transient
//    private List<Autor> autores;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "libros_autores",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<AutorEntity> autores;

    //@Enumerated(EnumType.STRING)
    private String idioma;
    private Integer numerodescargas;
//    @Transient
//    private List<Autor>;
//
    public LibroEntity(){

    }
    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public LibroEntity(String titulo, String autor, List<AutorEntity> autores, String idioma, Integer numerodescargas) {
        this.titulo = titulo;
        this.autor = autor;
        this.autores = autores;
        this.idioma = idioma;
        this.numerodescargas = numerodescargas;
    }


    @Override
    public String toString() {
        return  ", titulo='" + titulo + '\'' +
                ", autor='" + autores + '\'' +
                ", idioma='" + idioma + '\'' +
                ", numerodescargas=" + numerodescargas + '\'';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<AutorEntity> getAutores() {
        return autores;
    }

    public void setAutores(List<AutorEntity> autores) {
        this.autores = autores;
    }


    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumerodescargas() {
        return numerodescargas;
    }

    public void setNumerodescargas(Integer numerodescargas) {
        this.numerodescargas = numerodescargas;
    }
}
