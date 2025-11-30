# Test Plan

**Subject:** Diseño y Pruebas (Grado en Ingeniería del Software, Universidad de Sevilla)  
**Academic course:** 2025/2026 
**Group/Team:** LI-04  
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


| User Story | Test | Description | Status | Type |
|------------|------|-------------|--------|-------|
| US-01: Login | [UTB-1: AuthControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/auth/AuthControllerTests.java) | Verifies login with valid/invalid credentials. | Implemented | Isolated backend controller test |
| US-02: Register | [UTB-2: AuthServiceTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/auth/AuthServiceTests.java) | Verifies correct user registration and validations. | Implemented | Backend service test |
| US-03: Logout | [UTB-3: AuthControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/auth/AuthControllerTests.java) | Ensures logout invalidates session/token. | Implemented | Backend controller test |
| US-04: Delete my profile | [UTB-4: UserControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/user/UserControllerTests.java) | Ensures a user can delete their account. | Implemented | Backend controller test |
| US-05: Edit my profile | [UTB-5: UserControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/user/UserControllerTests.java) | Updates profile fields correctly. | Implemented | Backend controller test |
| US-06: Add a friend | [UTB-6: FriendshipRestControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/friendship/FriendshipRestControllerTests.java) | Verifies sending a friend request. | Implemented | Backend controller test |
| US-07: Delete a friend | [UTB-7: FriendshipRestControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/friendship/FriendshipRestControllerTests.java) | Ensures a friend can be removed. | Implemented | Backend controller test |
| US-08: Accept a friendship | [UTB-8: FriendshipServiceTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/friendship/FriendshipServiceTests.java) | Accepts pending friend requests. | Implemented | Backend service test |
| US-09: Reject a friendship | [UTB-9: FriendshipServiceTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/friendship/FriendshipServiceTests.java) | Rejects pending friend requests. | Implemented | Backend service test |
| US-10: Create a game | [UTB-10: GameRestControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/game/GameRestControllerTests.java) | Game/lobby creation. | Implemented | Backend controller test |
| US-11: Join a game | [UTB-11: GameRestControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/game/GameRestControllerTests.java) | Validates join by code. | Implemented | Backend controller test |
| US-12: Change deck | [UTB-12: GamePlayerServiceTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/gameplayer/GamePlayerServiceTests.java) | Ensures deck replacement works. | Implemented | Service test |
| US-13: Place a card | [UTB-13: ValidatorTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/model/ValidatorTests.java) | Validates card placement rules. | Implemented | Core rule validation test |
| US-14: Use "Speed Up" | [UTB-14: GamePlayerServiceTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/gameplayer/GamePlayerServiceTests.java) | Ensures 3-card turn logic. | Implemented | Gameplay logic test |
| US-15: Use "Reverse" | [UTB-15: GamePlayerServiceTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/gameplayer/GamePlayerServiceTests.java) | Validates alternate exit point logic. | Implemented | Gameplay logic test |
| US-16: Use "Brake" | [UTB-16: GamePlayerServiceTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/gameplayer/GamePlayerServiceTests.java) | Ensures 1-card turn effect. | Implemented | Gameplay logic test |
| US-17: Use "Extra Gas" | [UTB-17: GamePlayerCardServiceTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/gameplayercard/GamePlayerCardServiceTests.java) | Draws extra card. | Implemented | Gameplay card system test |
| US-18: Surrender | [UTB-18: GameRestControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/game/GameRestControllerTests.java) | Ends game upon surrender. | Implemented | Controller test |
| US-19: View rules during game | *(No backend test needed)* | Static rules page. | Not Applicable | UI-only |
| US-20: Return after viewing rules | *(No backend test needed)* | UI navigation only. | Not Applicable | UI-only |
| US-21: Chat with opponent | TBD | Message sending/receiving. | Missing | WebSocket test missing |
| US-22: Notify win/lose | [UTB-22: GameServiceTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/game/GameServiceTests.java) | Validates end-game events. | Implemented | Game conclusion logic |
| US-23: View games history | [UTB-23: GameServiceTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/game/GameServiceTests.java) | List of user games. | Implemented | Backend service |
| US-24: View achievements | [UTB-24: AchievementRestControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/achievement/AchievementRestControllerTests.java) | Fetches achievements list. | Implemented | Controller test |
| US-25: View stats | [UTB-25: AchievementServiceTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/achievement/AchievementServiceTests.java) | Stats/achievements consistency. | Implemented | Service test |
| US-26: Admin view ongoing games | [UTB-26: GameRestControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/game/GameRestControllerTests.java) | Returns active games. | Implemented | Controller test |
| US-27: Admin view past games | [UTB-27: GameRestControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/game/GameRestControllerTests.java) | Returns finished games. | Implemented | Controller test |
| US-28: Admin list users | [UTB-28: UserControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/user/UserControllerTests.java) | Lists all registered users. | Implemented | Controller test |
| US-29: Admin create user | [UTB-29: UserControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/user/UserControllerTests.java) | Creates a new user profile. | Implemented | Controller test |
| US-30: Admin delete user | [UTB-30: UserControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/user/UserControllerTests.java) | Deletes existing user. | Implemented | Controller test |
| US-31: Admin edit user | [UTB-31: UserControllerTests](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/src/test/java/es/us/dp1/lx_xy_24_25/endofline/user/UserControllerTests.java) | Updates user info. | Implemented | Controller test |



## 6. Acceptance Criteria

- All unit tests must pass successfully before the final project delivery.
- Code coverage should be at least 70%.
- There must be no critical failures in integration testing and functionality.

## 7. Conclusion

This test plan sets out the structure and criteria to ensure the quality of the software developed. It is the responsibility of the development and testing team to follow this plan to ensure a functional and error-free product delivery.
