package com.alura.literalura.model;

import jakarta.persistence.*;
import org.hibernate.sql.results.graph.FetchableContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;
    private Integer fechaNacimiento;
    private Integer fechaFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros = new ArrayList<>();

    public Autor(){

    }

        public Autor(Long id, String nombre, Integer fechaNacimiento, Integer fechaFallecimiento, List<Libro> libros) {
        this.id = id;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaFallecimiento = fechaFallecimiento;
        this.libros = libros;
    }

    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaFallecimiento = datosAutor.fechaFallecimiento();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(Integer fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        libros.forEach(l -> l.setAutor(this));
        this.libros = libros;
    }

    @Override
    public String toString() {
        String librosTitulos = libros.stream()
                .map(Libro::getTitulo)
                .collect(Collectors.joining(" | "));
        return "Autor: " + nombre +
                " (Nacimiento: " + (fechaNacimiento != null ? fechaNacimiento : "N/A") +
                ", Fallecimiento: " + (fechaFallecimiento != null ? fechaFallecimiento : "N/A") + ")" +
                "\n  Libros: [" + librosTitulos + "]";
    }
}
