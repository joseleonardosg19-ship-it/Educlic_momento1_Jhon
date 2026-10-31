https://educlicmomento1jhon-cpsui2nx.back4.app

Markdown
# Educlic API - Backend RESTful

API RESTful desarrollada con **Spring Boot** para la gestión e e-commerce de la plataforma **Educlic**. El sistema administra usuarios, productos, categorías, pedidos y autenticación, utilizando buenas prácticas de arquitectura con el patrón **DTO**, validación de datos, manejo global de excepciones y contenedorización con **Docker**.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17
* **Framework:** Spring Boot 3
* **Persistencia:** Spring Data JPA / Hibernate
* **Gestor de Dependencias:** Maven
* **Seguridad y Entornos:** Dotenv (Manejo de variables de entorno mediante `.env`)
* **Contenedorización:** Docker (Multi-stage build con `eclipse-temurin:17-jdk-alpine`)
* **Documentación & Pruebas:** Postman Collection

---

## 🏗️ Arquitectura y Estructura del Proyecto

El proyecto está organizado siguiendo una arquitectura en capas limpia e independiente:

```text
src/main/java/com/cesde/educlic/
├── config/          # Configuraciones globales (CORS, beans, etc.)
├── controller/      # Endpoints de la API REST
├── dto/             # Objetos de Transferencia de Datos (Request/Response DTOs)
├── exception/       # Manejo global de excepciones (@RestControllerAdvice)
├── model/           
│   ├── entity/      # Entidades de persistencia JPA
│   └── enums/       # Enumeradores (EstadoPedido, RolUsuario, etc.)
├── repository/      # Interfaces de acceso a datos (Spring Data JPA)
└── service/         # Lógica de negocio e integración de DTOs
Características Clave
Patrón DTO Plano: Las peticiones POST y PUT utilizan estructuras planas de JSON (ej. categoriaId, usuarioId) evitando datos anidados innecesarios y protegiendo el modelo de dominio.

Manejo Global de Excepciones: Respuestas HTTP uniformes para errores de negocio y recursos no encontrados mediante @RestControllerAdvice y excepciones personalizadas (ResourceNotFoundException).

Validación de Relaciones: Validación explícita de existencia de entidades mediante repositorios antes de la persistencia.

📦 Módulos de la API
Autenticación: Registro e inicio de sesión.

Usuarios: Gestión de perfiles y roles.

Categorías: Clasificación de productos.

Productos: Gestión de catálogo, precios, stock, tallas y colores.

Pedidos: Procesamiento de órdenes de compra con estado de seguimiento (PENDIENTE, PROCESANDO, ENVIADO, ENTREGADO, CANCELADO).

🚀 Despliegue y Ejecución Local
Prerrequisitos
Java 17 JDK

Git

Docker (Opcional, para ejecución en contenedor)

1. Clonar el repositorio
Bash
git clone [https://github.com/talzateg-netizen/Educlic_momento1_Jhon.git](https://github.com/talzateg-netizen/Educlic_momento1_Jhon.git)
cd Educlic_momento1_Jhon
2. Configurar variables de entorno
Crea un archivo .env en la raíz del proyecto basándote en .env.template (si aplica) con tus credenciales de base de datos:

Fragmento de código
PORT=8080
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/educlic_db
SPRING_DATASOURCE_USERNAME=tu_usuario
SPRING_DATASOURCE_PASSWORD=tu_contraseña
3. Ejecutar localmente con Maven
Bash
./mvnw spring-boot:run
4. Ejecutar con Docker
Para compilar y ejecutar la imagen ligera de Docker localmente:

Bash
# Construir la imagen
docker build -t educlic-api .

# Ejecutar el contenedor
docker run -p 8080:8080 --env-file .env educlic-api
🧪 Pruebas con Postman
El proyecto incluye la colección oficial actualizada de Postman en la raíz del repositorio:

Archivo: educlic-api.postman_collection.json

Cómo importar en Postman:
Abre Postman.

Haz clic en Import y selecciona el archivo educlic-api.postman_collection.json.

Configura la variable de entorno {{baseUrl}} apuntando a http://localhost:8080.
