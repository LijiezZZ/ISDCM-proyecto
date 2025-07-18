# Proyecto ISDCM - Plataforma de Gestión y Distribución de Vídeos

Este proyecto corresponde al desarrollo de una plataforma web para la gestión y distribución segura de contenido multimedia, en el marco de la asignatura **Internet, Seguridad y Distribución de Contenido Multimedia (ISDCM)**.

La aplicación permite a los usuarios registrarse, gestionar y reproducir vídeos vía streaming, integrando funcionalidades clave como la búsqueda de contenidos, el conteo de visualizaciones y medidas de seguridad web como autenticación y control de acceso.

El sistema se compone de:

- Un frontend en Java, desarrollado con NetBeans, que ofrece la interfaz gráfica al usuario.
- Un servicio backend RESTful en Java, encargado de la lógica de negocio y acceso a datos.
- Un proyecto adicional para pruebas de HTTPS con un login simple.

Durante el desarrollo se aplicaron buenas prácticas de diseño de software distribuido, manejo eficiente de contenido multimedia, e implementación de medidas de seguridad web.

## Integrantes

- Kenny Alejandro
- Lijie Yin

## Estructura del Proyecto

```
/frontend/             → Aplicación web en Java (NetBeans)
/frontend/javadoc/     → Documentación generada con Javadoc
/backend/              → API REST en Java
/backend/javadoc/      → Documentación generada con Javadoc
/httpsApp/             → Proyecto de prueba para HTTPS con login simple
```

## Entregas

### Primera Entrega

- Desarrollo de una aplicación web para la gestión de usuarios y vídeos.

### Segunda Entrega

- Integración con un servicio web RESTful.  
- Funcionalidad de búsqueda de vídeos.  
- Gestión del conteo de reproducciones.  
- Implementación de streaming de vídeo desde la app.

### Tercera Entrega

- Aplicación de técnicas de seguridad web:
  - Autenticación y autorización mediante JWT
  - Buenas prácticas de protección frente a amenazas comunes.

## Documentación Técnica
Puedes abrir el archivo `index.html` en tu navegador para explorar la documentación técnica.
La documentación generada con Javadoc se encuentra dentro del directorio:
```
/frontend/javadoc/apidocs/index.html
/backend/javadoc/apidocs/index.html
```
---
## Estructura de Comunicación y Flujo del Proyecto ISDCM

Este esquema resume la arquitectura lógica del sistema, separando las responsabilidades en capas bien definidas que interactúan entre sí siguiendo el principio de desacoplamiento y reutilización.

### Flujo general de ejecución

```
Frontend (JSP)
    ↓
Servlets Java
    ↓
ServicioVideoREST (modelo Video como DTO)
    ↓
API REST (VideoResource)
    ↓
VideoMapper (VideoDTO ↔ Entidad Video)
    ↓
Entidad Video (modelo de dominio)
    ↓
VideoDAO (acceso a base de datos)
```

### Descripción por componentes

#### 1. Frontend (JSP)
Páginas dinámicas que actúan como interfaz de usuario. Incluyen formularios para registrar, editar y listar vídeos. Utilizan validación del lado del cliente con JavaScript y Bootstrap.

#### 2. Servlets Java
Controladores web que procesan peticiones del usuario. Se encargan de validar la entrada, gestionar la sesión, redirigir vistas y delegar operaciones a `ServicioVideoREST`.

#### 3. ServicioVideoREST
Clase cliente HTTP en el frontend que se comunica con el backend mediante llamadas REST. Se encarga de serializar y deserializar objetos `Video` (en formato JSON) utilizando Jackson. Este modelo actúa como DTO en el contexto del frontend.

#### 4. API REST (VideoResource)
Exposición de servicios RESTful. Recibe solicitudes JSON, transforma datos con el `VideoMapper` y delega la lógica de negocio en `VideoDAO`. Define operaciones como búsqueda, edición, visualización y eliminación.

#### 5. VideoMapper
Clase de utilidad que convierte entre `VideoDTO` (usado en la capa REST) y `Video` (entidad de dominio). Encapsula la lógica de formateo de fechas, duración y timestamps.

#### 6. Entidad Video
Modelo de dominio persistente alineado con la estructura de la base de datos. Representa todos los campos de la tabla `VIDEOS`, incluyendo información como título, autor, duración, localización, usuario, etc.

#### 7. VideoDAO
Clase de acceso a datos que se comunica con la base de datos mediante JDBC. Ejecuta operaciones SQL para insertar, actualizar, consultar y eliminar vídeos.

---

## Incorporación de JWT

### Backend (API REST)

1. **Configuración Centralizada**:
   - Se creó el archivo `JwtConfig` para centralizar la clave secreta y el tiempo de expiración del token.

2. **Generación de JWT**:
   - El recurso `AuthResource` genera un JWT al autenticar al usuario mediante el endpoint `/login`.

3. **Validación de JWT**:
   - El filtro `JwtFilter` valida el token en cada petición REST, excepto en `/login`.

4. **Actualización del OpenAPI**:
   - Se añadió el esquema de seguridad `bearerAuth` en el archivo `openapi.json`.

### Frontend

1. **Autenticación**:
   - El servicio `ServicioAuthREST` realiza el login contra el backend y almacena el JWT.

2. **Peticiones Autenticadas**:
   - `ServicioVideoREST` incluye el JWT en la cabecera `Authorization` para todas las peticiones.

3. **Gestión del JWT**:
   - Se añadió una sección en la aplicación para visualizar el JWT y realizar operaciones de encriptación/desencriptación con JWE utilizando `JOSE4j`.

---

## Proyecto HTTPS (httpsApp)

Se añadió un proyecto adicional llamado `httpsApp` para realizar pruebas de HTTPS con un login simple. Este proyecto incluye:

1. **Servlet de Usuarios**:
   - Gestiona el login y la sesión del usuario.
   - Redirige al usuario a una página de inicio (`homepage.jsp`) tras un login exitoso.

2. **Páginas JSP**:
   - `login.jsp`: Formulario de inicio de sesión con validaciones.
   - `homepage.jsp`: Página de bienvenida que muestra el nombre del usuario autenticado.

3. **Configuración de HTTPS**:
   - Configuración básica para probar el uso de HTTPS en un entorno local.

---

## Cómo Ejecutar el Proyecto

### 📦 Backend (API REST)

1. Abre el proyecto **`isdcm-1.0`** en NetBeans.
2. GlassFish está configurado con un segundo dominio llamado `domain2`, ubicado en la ruta `/glassfish6/glassfish/domains/domain2`.
   - Puerto de aplicación: `8180`
   - Puerto de administración: `4849`
3. Ejecuta el proyecto desde NetBeans.
4. Accede a la API REST Videos en:  
   `http://localhost:8180/Backend/resources/videos`
  

### 🌐 Frontend (WebApp)

1. Abre el proyecto **`webApp1`** en NetBeans.
2. GlassFish está configurado con el dominio principal llamado `domain1`, ubicado en la ruta `/glassfish6/glassfish/domains/domain1`.
   - Puerto de aplicación: `8080`
   - Puerto de administración: `4848`
3. Ejecuta el proyecto desde NetBeans.
4. Accede a la app en:  
   `http://localhost:8080/Frontend/`
   
### 🔒 Proyecto HTTPS (httpsApp)

1. Abre el proyecto **`httpsApp`** en NetBeans.
2. Configura el servidor Tomcat como el servidor de aplicaciones para este proyecto.
3. Asegúrate de que el conector HTTPS esté habilitado en el archivo `server.xml` de Tomcat. La configuración básica es la siguiente:
    ```xml
    <Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
                maxThreads="150" SSLEnabled="true">
        <SSLHostConfig>
            <Certificate certificateKeystoreFile="${user.home}/.keystore"
                            certificateKeystorePassword="123456"
                            type="RSA" />
        </SSLHostConfig>
    </Connector>````
- El archivo .keystore debe estar ubicado en el directorio del usuario (${user.home}) y contener un certificado válido.
- Cambia la contraseña del keystore (123456) según tu configuración.
4. Ejecuta el proyecto desde NetBeans.
5. Accede al login en:
 `https://localhost:8443/httpsApp/vista/login.jsp`

---

### 📁 Repositorio de Vídeos

Los archivos subidos se guardan en:
```
/Frontend/videosRegistrados/
```

Asegúrate de que esa carpeta exista en tu ruta de proyecto local.

---

## Recomendaciones Técnicas

- Usar `java.time.LocalDate`, `LocalTime`, `OffsetDateTime` para gestionar fechas/hora.
- Todas las llamadas del frontend al backend se hacen vía `ServicioVideoREST.java`
- Se utiliza `ObjectMapper` (Jackson) para convertir objetos Java ↔ JSON.
- Las vistas JSP están en `/vista/` e incluyen validaciones con Bootstrap y JavaScript.

---

## Autenticación y Seguridad

- El sistema requiere inicio de sesión para acceder a las funcionalidades principales.
- Los servlets verifican sesión activa y restringen acciones si no hay permisos.
- Se utiliza JWT para autenticar las peticiones al backend.
- Se añadió soporte para encriptar y desencriptar el JWT en el frontend utilizando JWE.


## Autenticación y Seguridad

- El sistema requiere inicio de sesión para acceder a las funcionalidades principales.
- Los servlets verifican sesión activa y restringen acciones si no hay permisos.
- Se utiliza JWT para autenticar las peticiones al backend.
- Se añadió soporte para encriptar y desencriptar el JWT en el frontend utilizando JWE.
- Se realizaron pruebas de HTTPS con el proyecto `httpsApp`.

### Ubicación de las Claves

#### **Backend**
- La clave secreta utilizada para firmar y validar los JWT está definida en el archivo `JwtConfig.java`:
#### **Frontend**
- Las claves utilizadas para operaciones de cifrado están ubicadas en el directorio Frontend/Clave/:
    - claveXML.key: Usuada para el encriptado de XML.
    - claveVideo.key: Usada para el encriptado de vídeos.
    - claveJWE.key: Usada para el cifrado de JWE.
---

## Autoría Final

Proyecto realizado por: **Kenny Alejandro** y **Lijie Yin**  
Master en Ingeniería Informática - UPC
