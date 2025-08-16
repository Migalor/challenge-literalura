# 📚 Literalura

**Literalura** es una aplicación de consola desarrollada en **Java** con **Spring Boot** que permite buscar, registrar y consultar libros y autores desde la API pública [Gutendex](https://gutendex.com/).\
El proyecto es parte de un desafío de programación y utiliza **PostgreSQL** como base de datos para almacenar los datos consultados.

---

## ⚙️ Características

- Buscar libros por título a través de la API Gutendex.
- Registrar libros y autores en la base de datos si no existen.
- Mostrar todos los libros registrados.
- Mostrar todos los autores registrados.
- Consultar autores que estaban vivos en un año específico.
- Buscar libros por idioma (`es`, `en`, `fr`, `pt`).
- Evita duplicados: si el libro ya existe en la base de datos, muestra la información sin volver a registrarlo.


## 🚀 Cómo ejecutar

1. Clonar el repositorio:

```bash
git clone https://github.com/Migalor/challenge-literalura
cd literalura
```

2. Usar el menú interactivo en la consola:

```
1 - Buscar libro por título
2 - Mostrar libros registrados
3 - Mostrar autores registrados
4 - Mostrar autores vivos en un determinado año
5 - Mostrar libros por idioma
0 - Salir
```

---

## 📝 Ejemplo de uso

```
****************************
Bienvenido a Literalura
****************************
Ingrese el número de opción:
1
Ingrese el nombre del libro que desea buscar:
Don Quijote
----- LIBRO REGISTRADO -----
Título: Don Quijote
Autor: Miguel de Cervantes
Idioma: es
Número de descargas: 12345.0
---------------------------
```

Si el libro ya está registrado, mostrará:

```
El libro 'Don Quijote' ya está registrado.
------ LIBRO ------
Título: Don Quijote
Autor: Miguel de Cervantes
Idioma: es
Descargas: 12345.0
-------------------
```

