package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import com.alura.literalura.service.IConvierteDatos;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoAPI;
    private final IConvierteDatos conversor;
    private final String URL_BASE = "https://gutendex.com/books/";

    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(ConsumoAPI consumoAPI, ConsumoAPI consumoAPI1, IConvierteDatos conversor, LibroRepository libroRepository, AutorRepository autorRepository) {
        this.consumoAPI = consumoAPI1;
        this.conversor = conversor;
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraMenu(){
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
					\nElija la opción a través de su número:
					1 - Buscar libro por título
					2 - Mostrar libros registrados
					3 - Mostrar autores registrados
					4 - Mostrar autores vivos en un determinado año
					5 - Mostrar libros por idioma
					0 - Salir
                    """;
            System.out.println(menu);

            try {
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (Exception e) {
                System.out.println("Opción no valida");
                teclado.next();
            }

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;

                case 2:
                    mostrarLibrosRegistrados();
                    break;

                case 3:
                    mostrarAutoresRegistrados();
                    break;

                case 4:
                    mostrarAutoresVivosEnTiempo();
                    break;

                case 5:
                    mostrarLibrosPorIdioma();
                    break;

                case 0:
                    System.out.println("Cerrando la Aplicación...");
                    teclado.close();
                    System.exit(0);

            }
        }
    }

    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombreLibro.replace(" ", "+"));

        if (json == null || json.isEmpty()) {
            System.out.println("No se obtuvo respuesta de la API");
            return;
        }

        DatosResultados datosBusqueda = conversor.obtenerDatos(json, DatosResultados.class);
        Optional<DatosLibro> libroBuscado = datosBusqueda.resultados().stream().findFirst();

        if (libroBuscado.isPresent()) {
            DatosLibro datosLibro = libroBuscado.get();

            Optional<Libro> libroExistente = libroRepository.findByTituloContainsIgnoreCase(datosLibro.titulo());
            if (libroExistente.isPresent()){
                System.out.println("El libro " + libroExistente.get().getTitulo() + " ya está registrado");
                mostrarLibro(libroExistente.get());
                return;
            }

            DatosAutor datosAutor = datosLibro.autores().stream().findFirst().orElse(null);
            if (datosAutor == null) {
                System.out.println("No se encontró información");
                return;
            }

            Optional<Autor> autorExistente = autorRepository.findByNombreContainsIgnoreCase(datosAutor.nombre());
            Autor autor;
            if (autorExistente.isPresent()) {
                autor = autorExistente.get();
            } else {
                autor = new Autor(datosAutor);
                autor = autorRepository.save(autor);
            }

            Libro libro = new Libro(datosLibro);
            libro.setAutor(autor);
            libroRepository.save(libro);

            System.out.println("----- LIBRO REGISTRADO -----");
            mostrarLibro(libro);

        } else {
            System.out.println("Libro no encontrado con el título: '" + nombreLibro + "'");
        }
    }
   private void mostrarLibro(Libro libro) {
            System.out.println("--------- LIBRO ---------");
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre():"N/A"));
            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Número de descargas: " + libro.getNumeroDeDescargas());
            System.out.println("--------------------------\n");
   }

    private void mostrarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("----- LIBROS REGISTRADOS -----");
            libros.forEach(this::mostrarLibro);
            System.out.println("----------------------------\n");
        }
    }

    private void mostrarAutoresRegistrados(){
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()){
            System.out.println("No hay autores registrados");
        } else {
            System.out.println("----- AUTORES REGISTRADOS -----");
            autores.forEach(System.out::println);
            System.out.println("-----------------------------\n");
        }
    }
    private void mostrarAutoresVivosEnTiempo() {
        System.out.println("Ingrese el año para buscar autores vivos:");
        try {
            var tiempo = teclado.nextInt();
            teclado.nextLine();
            List<Autor> autoresVivos = autorRepository.findAutoresVivosEnTiempo(tiempo);
            if (autoresVivos.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + tiempo + ".");
            } else {
                System.out.println("----- AUTORES VIVOS EN " + tiempo + " -----");
                autoresVivos.forEach(System.out::println);
                System.out.println("----------------------------------\n");
            }
        } catch (Exception e) {
            System.out.println("Año no válido. Por favor, ingrese un número.");
            teclado.next();
        }
    }
    private void mostrarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma para buscar libros (es, en, fr, pt):");
        var idiomaInput = teclado.nextLine().toLowerCase();
        String idioma;

        switch (idiomaInput) {
            case "es":
                idioma = "es";
                break;
            case "en":
                idioma = "en";
                break;
            case "fr":
                idioma = "fr";
                break;
            case "pt":
                idioma = "pt";
                break;
            default:
                System.out.println("Idioma no válido. Intente con 'es', 'en', 'fr' o 'pt'.");
                return;
        }

        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma '" + idioma + "'.");
        } else {
            System.out.println("----- LIBROS EN IDIOMA '" + idioma.toUpperCase() + "' -----");
            librosPorIdioma.forEach(System.out::println);
            System.out.println("----------------------------------\n");
        }
    }

    public void muestraElMenu() {
    }
}


