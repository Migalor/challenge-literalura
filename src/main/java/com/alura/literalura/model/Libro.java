package com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;
    private String idioma;
    private Double numeroDeDescargas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro(){

    }

        public Libro(Long id, String titulo, String idioma, Double numeroDeDescargas, Autor autor) {
        this.id = id;
        this.titulo = titulo;
        this.idioma = idioma;
        this.numeroDeDescargas = numeroDeDescargas;
        this.autor = autor;
    }

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.idioma = datosLibro.idiomas() != null && !datosLibro.idiomas().isEmpty()
                ? datosLibro.idiomas().get(0)
                : "N/A";
        this.numeroDeDescargas = datosLibro.numeroDeDescargas();

        if (datosLibro.autores() != null && !datosLibro.autores().isEmpty()) {
            this.autor = new Autor(datosLibro.autores().get(0));
        }
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

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "------ LIBRO ------\n" +
                " Título: " + titulo + '\n' +
                " Autor: " + (autor != null ? autor.getNombre() : "N/A") + '\n' +
                " Idioma: " + idioma + '\n' +
                " Descargas: " + numeroDeDescargas + '\n' +
                "-------------------\n";
    }
}