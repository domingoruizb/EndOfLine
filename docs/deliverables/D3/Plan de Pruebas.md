# Test Plan

**Subject:** Diseño y Pruebas (Grado en Ingeniería del Software, Universidad de Sevilla)  
**Academic course:** 2025/2026 
**Group/Team:** LI-4  
**Name of the project:** End Of Line  
**Repository:** https://github.com/gii-is-DP1/dp1-2025-2026-li-4  
**Members (máx. 6):** 
Fernando José Fernández Fernández (HNR0360, ferferfer@alum.us.es),
Makar Lavrov (RRP9465, makar.lavrov.1@iliauni.edu.ge),
Angelo Sho Morachi (FLX0814, angmoraschi@gmail.com),
Domingo Ruiz Bellido (DYS4321, domruibel@alum.us.es),
Alejandro Urbina Tamayo (VMC1155, aleurbtam@alum.us.es)


## 1. Introduction

This document describes the test plan for the project **End Of Line** developed under the subject **Design and Testing 1** by the group **LI-4**. The objective of the test plan is to ensure that the software developed meets the requirements specified in the user histories and that the necessary tests have been carried out to validate its operation.

## 2. Scope

The scope of this test plan includes:

- Unit tests.
  - Backend unit tests including testing services or repositories
  - Frontend unit tests: tests of the javascript functions created in frontend.
  - User interface unit tests. Use the user interface of our frontend components.
- Integration tests.  In our case they are mainly driver tests that will also be run by JUnit.

## 3. Testing Strategy

### 3.1 Types of Tests

#### 3.1.1 Unit Tests
Unit tests shall be performed to verify the correct functioning of individual software components. Test automation tools such as **JUnit** in backend and jest in frontend will be used.

#### 3.1.2 Integration Testing
Integration tests will focus on evaluating the interaction between different modules or components of the system, we will perform them at API level, testing our Spring controllers.

## 4. Tools and Testing Environment

### 4.1 Tools
- **Maven**: Dependency management and test execution.
- **JUnit**: Unitary testing framework.
- **Jacoco**: Generation of code coverage reports. If you run the maven install command, the coverage report will be copied to the/docs/deliverables/D3/coverage repository subfolder (can be viewed by clicking on the index.html file in that directory).
- **Allure**: Generation of status reports of the latest test runs. Allows grouping tests by module/epic and feature. If the maven install command is executed, the status report will be copied to the/docs/deliverables/D3/status repository subfolder (can be viewed by clicking on the index.html file in that directory).
- **Jest**: Framework for unit tests in javascript.
- **React-test**: Library for creating unit tests of React components.

### 4.2 Test Environment
The tests will run in the development environment and eventually in the test environment of the continuous integration server.

## 5. Test Planning
### 5.1 Status and Traceability of Tests by Module and Epic

The test status report (with traceability of tests to modules and epic/user stories) can be found [here](
https://gii-is-dp1.github.io/group-project-seed/deliverables/D3/status/#behaviors).

### 5.2 Test Coverage

The test coverage report can be consulted [here](
https://gii-is-dp1.github.io/group-project-seed/deliverables/D3/coverage/).



*Nota importante para el alumno*: A la hora de entregar el proyecto, debes modificar la url para que esté asociada al respositorio concreto de tu proyecto. Date cuenta de que ahora mismo apunta al repositorio _gii-is-DP1/group-project-seed_.


| Historia de Usuario | Prueba | Descripción | Estado |Tipo |
|---------------------|--------|-------------|--------|--------|
| HU-01: Iniciar sesión | [UTB-1:TestLogin](https://github.com//gii-is-DP1/group-project-seed/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/your_game_name/auth/AuthControllerTest.java) | Verifica que un usuario puede iniciar sesión con credenciales válidas. | Implementada | Unitaria en backend de controlador aislaada |
| HU-02: Registrar usuario | [UTB-2:TestRegister](https://github.com//gii-is-DP1/group-project-seed/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/your_game_name/auth/AuthServiceTest.java) | Verifica que un nuevo usuario puede registrarse en el sistema. | Implementada |Unitaria en backend a nivel de Servicio, prueba social incluyendo a la BD y los repositorios. |

## 6. Acceptance Criteria

- All unit tests must pass successfully before the final project delivery.
- Code coverage should be at least 70%.
- There must be no critical failures in integration testing and functionality.

## 7. Conclusion

This test plan sets out the structure and criteria to ensure the quality of the software developed. It is the responsibility of the development and testing team to follow this plan to ensure a functional and error-free product delivery.
