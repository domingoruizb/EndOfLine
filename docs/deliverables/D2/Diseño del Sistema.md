# Documento de diseño del sistema
**Asignatura:** Diseño y Pruebas (Grado en Ingeniería del Software, Universidad de Sevilla)  
**Curso académico:** <!-- p.ej., 2025/2026 -->  
**Grupo/Equipo:** <!-- p.ej., L4-03 Equipo 33 -->  
**Nombre del proyecto:** <!-- p. ej. Petris -->  
**Repositorio:** <!-- URL del repo -->  
**Integrantes (máx. 6):** <!-- Nombre Apellidos (US-Id / correo @us.es) -->

_Esta es una plantilla que sirve como guía para realizar este entregable. Por favor, mantén las mismas secciones y los contenidos que se indican para poder hacer su revisión más ágil._ 

## Introducción

_En esta sección debes describir de manera general cual es la funcionalidad del proyecto a rasgos generales. ¿Qué valor puede aportar? ¿Qué objetivos pretendemos alcanzar con su implementación? ¿Cuántos jugadores pueden intervenir en una partida como máximo y como mínimo? ¿Cómo se desarrolla normalmente una partida?¿Cuánto suelen durar?¿Cuando termina la partida?¿Cuantos puntos gana cada jugador o cual es el criterio para elegir al vencedor?_

[Enlace al vídeo de explicación de las reglas del juego / partida jugada por el grupo](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/docs/Video.mp4)

## Diagrama(s) UML:

### Diagrama de Dominio/Diseño

_En esta sección debe proporcionar un diagrama UML de clases que describa el modelo de dominio, recuerda que debe estar basado en el diagrama conceptual del documento de análisis de requisitos del sistema pero que debe:_
•	_Especificar la direccionalidad de las relaciones (a no ser que sean bidireccionales)_
•	_Especificar la cardinalidad de las relaciones_
•	_Especificar el tipo de los atributos_
•	_Especificar las restricciones simples aplicadas a cada atributo de cada clase de domino_
•	_Incluir las clases específicas de la tecnología usada, como por ejemplo BaseEntity, NamedEntity, etc._
•	_Incluir los validadores específicos creados para las distintas clases de dominio (indicando en su caso una relación de uso con el estereotipo <<validates>>._

_Un ejemplo de diagrama para los ejercicios planteados en los boletines de laboratorio sería (hemos omitido las generalizaciones hacia BaseEntity para simplificar el diagrama):_

![Domain diagram](../../diagrams/domainDiagram_v1.png)

### Diagrama de Capas (incluyendo Controladores, Servicios y Repositorios)
_En esta sección debe proporcionar un diagrama UML de clases que describa el conjunto de controladores, servicios, y repositorios implementados, incluya la división en capas del sistema como paquetes horizontales tal y como se muestra en el siguiente ejemplo:_

![your-UML-diagram-name](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/gii-is-DP1/group-project-seed/main/docs/diagrams/LayersUMLPackageDiagram.iuml)

*Nota importante para el alumno*: A la hora de entregar el proyecto, debes modificar la url para que esté asociada al respositorio concreto de tu proyecto. Date cuenta de que ahora mismo apunta al repositorio _gii-is-DP1/group-project-seed_.


_El diagrama debe especificar además las relaciones de uso entre controladores y servicios, entre servicios y servicios, y entre servicios y repositorios._
_Tal y como se muestra en el diagrama de ejemplo, para el caso de los repositorios se deben especificar las consultas personalizadas creadas (usando la signatura de su método asociado)._

_En este caso, como mermaid no soporta la definición de paquetes, hemos usado una [herramienta muy similar llamada plantUML}(https://www.plantuml.com/). Esta otra herramienta tiene un formulario para visualizar los diagramas previamente disponible en [https://www.plantuml.com/plantuml/uml/}(https://www.plantuml.com/plantuml/uml/). Lo que hemos hecho es preparar el diagrama en ese formulario, y una vez teníamos el diagrama lista, grabarlo en un fichero aparte dentro del propio repositorio, y enlazarlo con el formulario para que éste nos genera la imagen del diagrama usando una funcionalizad que nos permite especificar el código del diagrama a partir de una url. Por ejemplo, si accedes a esta url verás el editor con el código cargado a partir del fichero del repositorio original: [http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/gii-is-DP1/group-project-seed/main/docs/diagrams/LayersUMLPackageDiagram.iuml](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/gii-is-DP1/group-project-seed/main/docs/diagrams/LayersUMLPackageDiagram.iuml)._

## Descomposición del mockups del tablero de juego en componentes

En esta sección procesaremos el mockup del tablero de juego (o los mockups si el tablero cambia en las distintas fases del juego). Etiquetaremos las zonas de cada una de las pantallas para identificar componentes a implementar. Para cada mockup se especificará el árbol de jerarquía de componentes, así como, para cada componente el estado que necesita mantener, las llamadas a la API que debe realizar y los parámetros de configuración global que consideramos que necesita usar cada componente concreto. 
Por ejemplo, para la pantalla de visualización de métricas del usuario en un hipotético módulo de juego social:

![Descomposición en componentes de la interfaz de estadísticas](https://github.com/gii-is-DP1/react-petclinic/assets/756431/12b36c37-39ed-422e-b8d9-56c94753cbdc)

  - App – Componente principal de la aplicación
    - $\color{orange}{\textsf{NavBar – Barra de navegación lateral}}$
      - $\color{darkred}{\textsf{[ NavButton ]. Muestra un botón de navegación con un icono asociado.}}$
    - $\color{darkblue}{\textsf{UserNotificationArea – Área de notificaciones e identificación del usuario actual}}$
    - $\color{blue}{\textsf{MetricsBar – En este componente se muestran las métricas principales del juego. Se mostrarán 4 métricas: partidas jugadas, puntos logrados, tiempo total, y cartas jugadas.}}$
      - $\color{darkgreen}{\textsf{[ MetricWell ] – Proporciona el valor y el incremento semanal de una métrica concreta. }}$
    - $\color{purple}{\textsf{GamesEvolutionChart – Muestra la tendencia de evolución en ellos últimos 4 meses en cuanto a partida jugadas, ganadas, perdidas y abandonadas.}}$
    - $\color{yellow}{\textsf{PopularCardsChart – Muestra la proporción de las N (parámetro de configuración) cartas más jugadas en el juego por el jugador.}}$
    - $\color{red}{\textsf{FrequentCoPlayersTable – Muestra los jugadores  con los que más se  ha jugado (de M en M donde M es un parámetro definido por la configuración del componente). Concretamente, se mostrarán la el nombre, la fecha de la última partida, la localización del jugador el porcentaje de partidas jugadas por ambos en las que el usuario ha ganado y si el jugador es amigo o no del usuario.}}$

## Design and architectural patterns designed
In this section, specify the set of design and architectural patterns applied during the project. To specify the application of each pattern, you can use the following template:

### Pattern: Single Page Application
*Tipo*: Design

*Application context*

The SPA pattern was applied in the frontend, specifically using the React library to manage component-based views and state, concerning all source files in the src/frontend directory.

*Classes or packages created*

To implement the pattern, the code residing in the src/frontend directory was created, including key components such as the Router component (e.g., using React Router).

*Advantages achieved by applying the pattern*

The pattern constitutes an advantage when building web applications, as it allows for more fluid response times, an overall better user experience and a cleaner separation of concerns between frontend (presentation/logic) and backend (API/data).

### Pattern: Model View Controller (MVC)
*Tipo*: Architectural

*Application context*

The MVC pattern was applied as a layered architecture across the project. In the Backend, it structures the API logic and data access. In the Frontend (SPA), it structures the presentation layer, separating the UI (View) from the application's state management (Model).

*Classes or packages created*

The project is structured in two main areas:

- Backend (src/main/java/...): Includes the Controllers (e.g., UserRestController.java) and the Model/Logic layer via Service and Repository classes (e.g., UserService.java, UserRepository.java), which manage the in-memory data.

- Frontend (frontend/src): Implements the View component through UI components.

*Advantages achieved by applying the pattern*

The MVC pattern allows us to easily separate the implemented functionality, achieving high cohesion and low coupling:

- Business logic (residing in the Services and Model).

- Presentation (residing in the View).

- User interface event handling (residing in the Controller).

### Pattern: Container/Presentational components
*Tipo*: Design

*Application context*

This design pattern was implemented across the entire frontend application (SPA) to clearly separate logic and data fetching from UI rendering. The MyProfile component serves as a prime example of this pattern.

*Classes or packages created*

The structure clearly separates the concerns:

- Container Component (Logic): The MyProfile.js file itself acts as the Container (Smart Component). It handles:

  - State management (useState, useFetchState).

  - Business logic (input validations, handleSaveChanges).

  - Data fetching (useEffect).

  - API communication (fetch calls).

  - It uses external Service (tokenService) and Utility (getErrorModal, deleteMyself) functions to keep its own logic clean.

- Presentational Components (UI/View): The component delegates the pure rendering responsibilities to reusable UI components, such  as the imported Modal, Container, Input, and Button components from reactstrap.

*Advantages achieved by applying the pattern*

The pattern enforces Separation of Concerns by dividing component roles, resulting in:

- Improved Reusability: Presentational components can be reused anywhere, as they are decoupled from the application's specific state.

- Easier Maintenance: Logic changes (Container) are separated from UI changes (Presentational), simplifying debugging and updates.

- Clarity: It clearly defines what the application does (Container) versus how it looks (Presentational).

### Pattern: Dependency Injection
*Tipo*: Design

*Application context*

The Dependency Injection (DI) pattern is fundamental to the Backend (Java/Spring Boot) architecture, specifically ensuring loose coupling between the application's layers (Controllers, Services, and Repositories). It is applied universally to manage the dependencies between the application's components, known as Beans.

*Classes or packages created*

The pattern's application is evident in how components are structured to receive dependencies, rather than create them:

- The UserRestController.java file is a concrete example, where the UserService and AuthoritiesService dependencies are requested via the constructor.

- The framework automatically provides instances of components like the Services (Service.java) and Repositories (Repository.java) wherever they are needed.

*Advantages achieved by applying the pattern*

The application of DI results in significant architectural benefits for the backend:

- Loose Coupling: Components only declare what dependencies they need, without knowing how those dependencies are instantiated.

- Easier Maintenance and Testing: It allows for easy substitution of mock components during unit testing.

### Pattern: Proxy pattern
*Tipo*: Design

*Application context*

The Proxy pattern is used implicitly by the Spring Framework in the Backend (Java/Spring Boot) architecture. This pattern is essential for adding transactional behavior, security checks, and other cross-cutting concerns to the Service layer without modifying the original code.

*Classes or packages created*

No code was manually created. The Spring framework automatically generates a Proxy object that wraps the Service components (like UserService) when features like database transactions (@Transactional) need to be applied.

*Advantages achieved by applying the pattern*

The pattern allows the framework to create a placeholder that adds functionality transparently:

- Transparent Functionality: Adds features (e.g., managing a database transaction) before and after a method runs, keeping your core business logic clean.

- Separation of Concerns: Separates the infrastructure logic (Proxy) from the business logic (Service).

### Pattern: Front Controller
*Tipo*: Design

*Application context*

The pattern is used in the Backend (Spring Boot) to ensure all incoming web requests are handled by a single, central component.

*Classes or packages created*

No classes were manually created. The pattern is implemented automatically by the Spring component, the DispatcherServlet.

*Advantages achieved by applying the pattern*

- Centralization: All requests pass through one point, making it easy to apply cross-cutting logic like security or logging globally.

- Simple Routing: Decouples request receiving from request processing.

### Pattern: Domain Model
*Tipo*: Design

*Application context*

The pattern is used in the Backend to define the core business logic and data structures.

*Classes or packages created*

The Entity classes (such as User.java and PlayerAchievement.java) and all domain packages (e.g., game/, friendship/) are the direct implementation of this pattern.

*Advantages achieved by applying the pattern*

- Single Source of Truth: Entities define the application's rules and relationships.

- Separation from Persistence: It keeps business logic separate from the data access layer (Repository Pattern).

### Pattern: Service Layer
*Tipo*: Design

*Application context*

The Service Layer pattern is used in the Backend to encapsulate the core business logic and isolate it from Controllers and Repositories.

*Classes or packages created*

The pattern is implemented by the Service classes (e.g., UserService.java, AuthoritiesService.java), which reside in the service layer of your domain packages.

*Advantages achieved by applying the pattern*

- Decoupling: Separates complex business rules from the user interface and API handling.

- Transaction Management: It is the centralized location for applying transaction boundaries, ensuring data consistency.

- Reusability: Business logic can be reused across different parts of the application.

### Pattern: Data Mapper
*Tipo*: Architectural

*Application context*

The pattern is used in the Backend to separate the Domain Model (Entities) from the database.

*Classes or packages created*

It is implemented through the Repository interfaces (e.g., UserRepository.java), which act as gateways to the database.

*Advantages achieved by applying the pattern*

- Domain Purity: Keeps business logic clean of data access code.

- Independence: Allows the database technology to be changed without affecting the application core.

### Pattern: Repository
*Tipo*: Design

*Application context*

The pattern is used in the Backend to handle all data access. It lets the application work with data as if it were a simple collection of objects.

*Classes or packages created*

It is implemented by the Repository interfaces (e.g., UserRepository.java). These interfaces contain all the methods needed to save, find, or delete data.

*Advantages achieved by applying the pattern*

- Simple Data Access: Business logic doesn't need to know how the data is stored.

- Decoupling: Clearly separates the business logic (Service Layer) from the data storage specifics.

### Pattern: Hooks
*Tipo*: Design

*Application context*

This pattern is used in the Frontend to manage state and logic within functional components (instead of classes).

*Classes or packages created*

It is implemented through core React functions like useState, useEffect, and useNavigate. It is also used to create Custom Hooks (e.g., useFetchState) for reusing logic.

*Advantages achieved by applying the pattern*

- Organization: Allows grouping related logic (e.g., data fetching) into a single reusable function.

- Simplicity: Makes components shorter and easier to read and maintain.

## Decisiones de diseño
_En esta sección describiremos las decisiones de diseño que se han tomado a lo largo del desarrollo de la aplicación que vayan más allá de la mera aplicación de patrones de diseño o arquitectónicos._

### Decisión X
#### Descripción del problema:*

Describir el problema de diseño que se detectó, o el porqué era necesario plantearse las posibilidades de diseño disponibles para implementar la funcionalidad asociada a esta decisión de diseño.

#### Alternativas de solución evaluadas:
Especificar las distintas alternativas que se evaluaron antes de seleccionar el diseño concreto implementado finalmente en el sistema. Si se considera oportuno se pude incluir las ventajas e inconvenientes de cada alternativa

#### Justificación de la solución adoptada

Describir porqué se escogió la solución adoptada. Si se considera oportuno puede hacerse en función de qué  ventajas/inconvenientes de cada una de las soluciones consideramos más importantes.
Os recordamos que la decisión sobre cómo implementar las distintas reglas de negocio, cómo informar de los errores en el frontend, y qué datos devolver u obtener a través de las APIs y cómo personalizar su representación en caso de que sea necesario son decisiones de diseño relevantes.

_Ejemplos de uso de la plantilla con otras decisiones de diseño:_

### Decisión 1: Importación de datos reales para demostración
#### Descripción del problema:

Como grupo nos gustaría poder hacer pruebas con un conjunto de datos reales suficientes, porque resulta más motivador. El problema es al incluir todos esos datos como parte del script de inicialización de la base de datos, el arranque del sistema para desarrollo y pruebas resulta muy tedioso.

#### Alternativas de solución evaluadas:

*Alternativa 1.a*: Incluir los datos en el propio script de inicialización de la BD (data.sql).

*Ventajas:*
•	Simple, no requiere nada más que escribir el SQL que genere los datos.
*Inconvenientes:*
•	Ralentiza todo el trabajo con el sistema para el desarrollo. 
•	Tenemos que buscar nosotros los datos reales

*Alternativa 1.b*: Crear un script con los datos adicionales a incluir (extra-data.sql) y un controlador que se encargue de leerlo y lanzar las consultas a petición cuando queramos tener más datos para mostrar.
*Ventajas:*
•	Podemos reutilizar parte de los datos que ya tenemos especificados en (data.sql).
•	No afecta al trabajo diario de desarrollo y pruebas de la aplicación
*Inconvenientes:*
•	Puede suponer saltarnos hasta cierto punto la división en capas si no creamos un servicio de carga de datos. 
•	Tenemos que buscar nosotros los datos reales adicionales

*Alternativa 1.c*: Crear un controlador que llame a un servicio de importación de datos, que a su vez invoca a un cliente REST de la API de datos oficiales de XXXX para traerse los datos, procesarlos y poder grabarlos desde el servicio de importación.

*Ventajas:*
•	No necesitamos inventarnos ni buscar nosotros lo datos.
•	Cumple 100% con la división en capas de la aplicación.
•	No afecta al trabajo diario de desarrollo y pruebas de la aplicación
*Inconvenientes:*
•	Supone mucho más trabajo. 
•	Añade cierta complejidad al proyecto

*Justificación de la solución adoptada*
Como consideramos que la división en capas es fundamental y no queremos renunciar a un trabajo ágil durante el desarrollo de la aplicación, seleccionamos la alternativa de diseño 1.c.

## Refactorizaciones aplicadas

Si ha hecho refactorizaciones en su código, puede documentarlas usando el siguiente formato:

### Refactorización X: 
En esta refactorización añadimos un mapa de parámtros a la partida para ayudar a personalizar la información precalculada de la que partimos en cada fase del juego.
#### Estado inicial del código
```Java 
class Animal
{
}
``` 
_Puedes añadir información sobre el lenguaje concreto en el que está escrito el código para habilitar el coloreado de sintaxis tal y como se especifica en [este tutorial](https://docs.github.com/es/get-started/writing-on-github/working-with-advanced-formatting/creating-and-highlighting-code-blocks)_

#### Estado del código refactorizado

```
código fuente en java, jsx o javascript
```
#### Problema que nos hizo realizar la refactorización
_Ej: Era difícil añadir información para implementar la lógica de negocio en cada una de las fases del juego (en nuestro caso varía bastante)_
#### Ventajas que presenta la nueva versión del código respecto de la versión original
_Ej: Ahora podemos añadir arbitrariamente los datos que nos hagan falta al contexto de la partida para que sea más sencillo llevar a cabo los turnos y jugadas_
