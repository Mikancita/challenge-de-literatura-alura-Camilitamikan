Challenge Literatura – Gutendex + PostgreSQL (Consola)

Aplicación Java que consume la API pública de Gutendex (Project Gutenberg) y permite consultar y persistir libros y autores en PostgreSQL.
Interfaz por consola con 5 opciones principales y 3 tablas: libros, autores, libros_autores.
Base de datos: literatura.

🧭 Menú (consola)
1 - Buscar por título (consulta Gutendex y guarda resultados)
2 - Buscar libros registrados
3 - Lista de autores registrados
4 - Lista de autores vivos en determinado año
5 - Lista de libros por idioma
0 - Salir

🛠️ Stack

Java 17

Spring Boot / Spring Data JPA (o JPA/Hibernate equivalente según tu setup)

PostgreSQL

Maven

HttpClient / ObjectMapper para consumo y mapeo de la API

Si no usas Spring Boot, adapta la sección de ejecución. La lógica y estructura siguen siendo válidas.

🗄️ Esquema de Base de Datos

Relación N:M entre libros y autores.

literatura
├─ autores (id, nombre, anio_nacimiento, anio_fallecimiento, ... )
├─ libros  (id, titulo, idioma, descargas, ... )
└─ libros_autores (libro_id, autor_id)  -- tabla puente


SQL de ejemplo (crea solo la BD, las tablas las genera JPA si usas ddl-auto):

CREATE DATABASE literatura;

⚙️ Configuración
1) Clonar
git clone https://github.com/Mikancita/challenge-de-literatura-alura-Camilitamikan.git
cd challenge-de-literatura-alura-Camilitamikan

2) Configurar PostgreSQL

src/main/resources/application.properties (o tu archivo de config):

spring.datasource.url=jdbc:postgresql://localhost:5432/literatura
spring.datasource.username=postgres
spring.datasource.password=TU_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true


Si no usas Spring Boot, configura tu persistence.xml y el pool de conexiones equivalentes.

▶️ Ejecución

Con Spring Boot:

mvn spring-boot:run


Sin Spring Boot (jar ejecutable):

mvn clean package
java -jar target/literatura-*.jar


Al iniciar verás el menú por consola y podrás interactuar con las 5 opciones.

🔍 ¿Qué hace cada opción?
1) Buscar por título

Pide un texto y consulta Gutendex:
https://gutendex.com/books/?search=<titulo>

Mapea el JSON a objetos (título, autores, idiomas, descargas).

Persiste los resultados en libros, autores y libros_autores (evita duplicados).

Muestra en consola: Título, Autor(es), Idioma (ISO) y Descargas.

2) Buscar libros registrados

Lista todos los libros desde la BD (sin llamar a la API).

3) Lista de autores registrados

Recorre los libros guardados y arma un set de autores ordenado.

4) Autores vivos en determinado año

Pide un año y filtra autores cuyo rango de vida incluye ese año.

5) Lista de libros por idioma

Muestra idiomas disponibles en la BD (EN, ES, FR, …).

Pide un código y lista los libros que coinciden (consulta local).

🌐 Notas sobre la API (Gutendex)

Endpoint base: https://gutendex.com/books/

Respuesta contiene campos como title, authors[], languages[], download_count.

Se sugiere guardar el primer idioma si viene un arreglo de varios.

✨ Próximas mejoras

Cache local para evitar repetir llamadas iguales.

Paginación y filtros avanzados (descargas mínimas, múltiples idiomas).

Exportación a CSV/JSON desde los registros locales.

Tests automatizados.

👩‍💻 Autora

Camila Mikan
Correo: camilitamikan@hotmail.com

GitHub: @Mikancita
