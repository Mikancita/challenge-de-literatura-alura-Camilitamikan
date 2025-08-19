package com.example.Literatura.principal;
import com.example.Literatura.Repository.AutorRepository;
import com.example.Literatura.model.*;
import com.example.Literatura.service.ConsumoApi;
import com.example.Literatura.service.ConvierteDatos;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.example.Literatura.Repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

import java.util.Scanner;
import java.util.stream.Stream;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosLibros> datosLibros = new ArrayList<>();
    private LibroRepository repositorio;
    private final AutorRepository autorRepository;
    private Idioma obtenerIdiomaPrincipal(DatosLibros libro) {
        if (libro.idioma() == null || libro.idioma().isEmpty()) {
            return Idioma.OTRO;
        }
        return Idioma.fromString(libro.idioma().get(0));
    }


    public Principal(LibroRepository repositorio, AutorRepository autorRepository) {
        this.repositorio = repositorio;
        this.autorRepository = autorRepository;
    }


    public void muestraElMenu(){
        var opcion = -1;
        while (opcion !=0){
            var menu="""
            1 - Buscar por título
            2 - Buscar libros registrados
            3 - Lista de autores registrados
            4 - Lista de autores vivos en determinado año
            5 - Lista de libros por idioma
            
            0 - Salir
            """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion){
                case 1:
                    buscarPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnAno();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                default:
                    System.out.println("Opción invalida");
            }
        }
    }

    private DatosLibros getDatosLibros() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+"));
        System.out.println(json);
        DatosLibros datos = conversor.obtenerDatos(json, DatosLibros.class);
        return datos;
    }


    private void buscarPorTitulo() {
        System.out.println("Escribe el título que deseas buscar:");
        var titulo = teclado.nextLine();

        try {
            String encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            var json = consumoApi.obtenerDatos(URL_BASE + "?search=" + encodedTitulo);
            var wrapper = conversor.obtenerDatos(json, Wrapper.class);

            for (DatosLibros libro : wrapper.results()) {
                System.out.println("----- LIBRO -----");
                System.out.println("Título: " + libro.titulo());

                String autor = "Desconocido";
                if (libro.autores() != null && !libro.autores().isEmpty()) {
                    autor = libro.autores().get(0).nombre();
                }

                System.out.println("Autor: " + autor);
                System.out.println("Idioma: " + obtenerIdiomaPrincipal(libro).getNombre());
                System.out.println("Descargas: " + libro.Numerodescargas());
                System.out.println("-----------------");

                // Persistir si no existe en base de datos
                Optional<LibroEntity> existente = repositorio.findByTituloContainsIgnoreCase(libro.titulo());
                if (existente.isEmpty()) {
                    List<AutorEntity> listaAutores = libro.autores() != null
                            ? convertirYGuardarAutores(libro.autores())
                            : new ArrayList<>();
                    String autorTexto = (libro.autores() != null && !libro.autores().isEmpty())
                            ? libro.autores().get(0).nombre()
                            : "Anonymous";
                    var libroEntity = new LibroEntity(
                            libro.titulo(),
                            autorTexto,         // <-- nuevo
                            listaAutores,
                            obtenerIdiomaPrincipal(libro).name(),
                            libro.Numerodescargas()
                    );

                    repositorio.save(libroEntity);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar libros: " + e.getMessage());
        }
    }
    private void listarLibrosRegistrados() {
        System.out.println("----- LISTADO DE LIBROS REGISTRADOS -----");

        List<LibroEntity> libros = repositorio.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            for (LibroEntity libro : libros) {
                System.out.println("Título: " + libro.getTitulo());
                System.out.println("Autor: " + libro.getAutor());
                System.out.println("Idioma: " + libro.getIdioma());
                System.out.println("Descargas: " + libro.getNumerodescargas());
                System.out.println("-----------------------------------------");
            }
        }
    }
    private void listarAutoresRegistrados() {
        System.out.println("----- AUTORES REGISTRADOS -----");

        List<LibroEntity> libros = repositorio.findAll();

        // Extraer autores únicos y ordenarlos alfabéticamente
        Set<String> autores = libros.stream()
                .flatMap(libro -> libro.getAutores() != null
                        ? libro.getAutores().stream().map(AutorEntity::getNombre)
                        : Stream.empty())
                .filter(nombre -> nombre != null && !nombre.isBlank())
                .collect(Collectors.toCollection(TreeSet::new));


        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            autores.forEach(autor -> System.out.println("Autor: " + autor));
        }

        System.out.println("-------------------------------");
    }
    private void listarAutoresVivosEnAno() {
        System.out.print("Ingresa el año para consultar autores vivos: ");
        int anio = teclado.nextInt();
        teclado.nextLine(); // Limpia el buffer

        List<LibroEntity> libros = repositorio.findAll();

        Set<AutorEntity> autoresVivos = new TreeSet<>(Comparator.comparing(AutorEntity::getNombre));

        for (LibroEntity libro : libros) {
            if (libro.getAutores() != null) {
                for (AutorEntity autor : libro.getAutores()) {
                    boolean nacioAntesOEnEseAnio = autor.getNacimiento() != null && autor.getNacimiento() <= anio;
                    boolean murioDespuesODesconocido = autor.getFallecimiento() == null || autor.getFallecimiento() >= anio;

                    if (nacioAntesOEnEseAnio && murioDespuesODesconocido) {
                        autoresVivos.add(autor);
                    }
                }
            }
        }

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio);
        } else {
            System.out.println("Autores vivos en el año " + anio + ":");
            autoresVivos.forEach(a -> System.out.println(" " + a.getNombre() + " (" + a.getNacimiento() + " - " +
                    (a.getFallecimiento() == null ? "¿Vive aún?" : a.getFallecimiento()) + ")"));
        }
    }
    private void listarLibrosPorIdioma() {
        List<LibroEntity> libros = repositorio.findAll();

        // Mostrar idiomas disponibles
        Set<String> idiomas = libros.stream()
                .map(LibroEntity::getIdioma)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));

        System.out.println("Idiomas disponibles en la base de datos: " + idiomas);
        System.out.print("Ingresa el idioma (ej: EN, ES, FR, OTRO): ");
        String idiomaBuscado = teclado.nextLine().trim().toUpperCase();

        // Filtrar libros por idioma
        List<LibroEntity> librosFiltrados = libros.stream()
                .filter(libro -> idiomaBuscado.equalsIgnoreCase(libro.getIdioma()))
                .toList();

        // Mostrar resultados
        if (librosFiltrados.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma: " + idiomaBuscado);
        } else {
            System.out.println("Libros en el idioma " + idiomaBuscado + ":");
            for (LibroEntity libro : librosFiltrados) {
                System.out.println("Título: " + libro.getTitulo());
                System.out.println("Idioma: " + libro.getIdioma());
                System.out.println("Descargas: " + libro.getNumerodescargas());
                System.out.println("-----------------------------------");
            }
        }
    }

    private List<AutorEntity> convertirYGuardarAutores(List<Autor> autores) {
        List<AutorEntity> entidades = new ArrayList<>();

        for (Autor autor : autores) {
            Optional<AutorEntity> existente = autorRepository.findByNombre(autor.nombre());

            AutorEntity autorEntity = existente.orElseGet(() -> {
                AutorEntity nuevo = new AutorEntity(
                        autor.nombre(),
                        autor.nacimiento(),
                        autor.fallecimiento()
                );
                return autorRepository.save(nuevo);
            });

            entidades.add(autorEntity);
        }

        return entidades;
    }

}
