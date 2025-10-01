# Documento de análisis de requisitos del sistema
**Asignatura:** Diseño y Pruebas (Grado en Ingeniería del Software, Universidad de Sevilla)  
**Curso académico:** 2025/2026 
**Grupo/Equipo:** LI-04  
**Nombre del proyecto:** End of Line  
**Repositorio:** https://github.com/gii-is-DP1/dp1-2025-2026-li-4
**Integrantes (máx. 6):** Fernando José Fernández Fernández (HNR0360  / ferferfer@alum.us.es), Angelo Sho Moraschi (FLX0814  / angmor@alum.us.es), Alejandro Urbina Tamayo (VMC1155 / aleurbtam@alum.us.es), Makar Lavrov (RRP9465 / maklav@alum.us.es), Domingo Ruiz Bellido (DYS4321 / domruibel@alum.us.es).

_Esta es una plantilla que sirve como guía para realizar este entregable. Por favor, mantén las mismas secciones y los contenidos que se indican para poder hacer su revisión más ágil._ 

## Introducción

The project focuses on the implementation of the board game End of Line which is a strategic card-based board game where the main objective is simple: cut your opponent’s line before they cut yours. Players build paths by placing Line Cards on a shared grid, managing both placement and limited Energy Points to alter the flow of the game.

The game supports 2 players. The playing area is a 7x7 grid.

A typical match unfolds in rounds, each consisting of drawing cards and placing them to extend your line, always respecting directional connections. From the third round onward, players may spend energy to gain advantages such as speeding up, slowing down, or redirecting their line.

Games usually last between 15 and 40 minutes, depending on their choices. The match ends immediately when a player cannot continue their line on their turn — that player loses, and the opponent is declared the winner.

[Link to the video explaining the rules and playing a game](https://github.com/gii-is-DP1/dp1-2025-2026-li-4/blob/main/docs/Video.mp4)

## Tipos de Usuarios / Roles

- Player: a user registered on the platform who can join matches, manage their profile, view statistics and his/her list of games, and actively participate in games.
- Administrator: A user with platform management privileges who can view a list of ongoing games (including the creator and participating users), a list of completed games and their participants and maintain platform operation as needed.
- Guest: A person who only has the ability to either create a new account or log in to an existing one.

## Historias de Usuario

Next, they are defined all user story to be implemented.

### US#1-(ISSUE#nn): Login ([Issue]())

| "As a guest, I want to sign in to my account so that I can view my information and start playing"|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. The user selects the “Log In” option on the initial screen.  |
|2. The Login page is displayed, where the user provides their credentials (username and password).  |
|3. After clicking the “Log In” button, if the credentials are valid, the user is taken to the Dashboard page.  |

### US#2-(ISSUE#nn): Register ([Issue]())

| "As a guest, I want to create a new account so that I can start playing."|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. The user clicks the “Register” button on the initial screen, which opens the Registration page.  |
|2. To complete the registration, the user provides their name, surname, username, password, email, and date of birth.  |

### US#3-(ISSUE#nn): Logout ([Issue]())

| "As a player/administrator, I want to log out of my account so that no one else can access it from the same device."|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the player/administrator clicks the "Logout" button located at the top-right corner.  |
|2. A popup is shown just to ask if the player/administrator is sure about logging out.  |
|3. After clicking on "Yes", the user is signed out of the system.  |

### US#4-(ISSUE#nn): Delete my profile ([Issue]())

| "As a player/administrator, I want to remove my account so that all my data is erased from the system."|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the player/administrator clicks his/her profile button, located in the top-right corner.  |
|2. A menu will appear showing several options, including the "Edit Profile" feature.  |
|3. The player selects "Edit Profile", which takes him/her to the "My Profile" screen.  |
|4. From there, if the player click the "Delete Profile" button and confirm by selecting "Yes" on the confirmation popup, his/her profile will be removed from the system.  |

### US#5-(ISSUE#nn): Edit my profile ([Issue]())

| "As a player/administrator, I want to edit my profile so that I can update my personal information."|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the player/administrator clicks his/her profile button, located in the top-right corner.  |
|2. A menu will appear showing several options, including the "Edit Profile" feature.  |
|3. The player select "Edit Profile," which takes him/her to the "My Profile" screen.  |
|4. From there, clicking on the "Edit profile" button takes him/her to the "Edit profile" screen. |
|5. Once typed all the changes, he/she will save them by clicking the "Save changes" button. |

### US#6-(ISSUE#nn): Add a friend ([Issue]())

| "As a player, I want to add a friend so that I can play with him/her."|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the player has to click the his/her profile button. |
|2. A menu will appear showing several options, including the "Friendships" feature. |
|3. The player selects "Friendships", which takes him/her to the "Friendships" screen. |
|4. From there, there is a "Create" button and pressing it, the player is taken to "Create Friendship" screen.  |
|5. From there, the player can type the username of his new friend and send the invitation.  |

### US#7-(ISSUE#nn): Delete a friend ([Issue]())

| "As a player, I want to delete a friend so that I don't want to play more with him/her."|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the player has to click the his/her profile button. |
|2. A menu will appear showing several options, including the "Friendships" feature. |
|3. The player selects "Friendships", which takes him/her to the "Friendships" screen. |
|4. From there, the player can see the list of his friends and can press the "Delete" button of the friendship he/she wants to remove.  |
|5. A popup will appear asking if the player is sure about the deletion, pressing "Yes", the friendship will be deleted.  |

### US#8-(ISSUE#nn): Accept a friendship ([Issue]())

| "As a player, I want to accept a friendship so that I can play with the friend requester."|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the player has to click the his/her profile button. |
|2. A menu will appear showing several options, including the "Friendships" feature. |
|3. The player selects "Friendships", which takes him/her to the "Friendships" screen. |
|4. From there, the player press the "Pending" button and takes him/her to the "Pending Invites" screen. |
|5. From there, the player can view any pending invitations and accept them using the "Accept" button.  |

### US#9-(ISSUE#nn): Reject a friendship ([Issue]())

| "As a player, I want to reject a friendship so that I don't want to play with the friend requester."|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the player has to click the his/her profile button. |
|2. A menu will appear showing several options, including the "Friendships" feature. |
|3. The player selects "Friendships", which takes him/her to the "Friendships" screen. |
|4. From there, the player press the "Pending" button and takes him/her to the "Pending Invites" screen. |
|5. From there, the player can view any pending invitations and reject them using the "Reject" button.  |

### US#10-(ISSUE#nn): Create a game ([Issue]())

| "As a player, I want to create a new game so that other players can join it."|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the player clicks has to click the "Play now!" button and the "Create Game" screen game will be opened.  |
|2. The player has to select which one of his/her friends wants to play with and which colour card he/she wants. |
|3. When pressing the "Continue" button, the game starts.  |

### US#11-(ISSUE#nn): Join a game ([Issue]())

| "As a player, I want to enter a game so that I can play alongside the another player who has invited me."|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the header of the home screen, an invitation message will appear showing that a friend invited the player to a game.  |
|2. The player has to click on the "Accept" button to start the game. |
|3. The player is taken to the "Game" screen and the game starts.  |

### US#12-(ISSUE#nn): List player games ([Issue]())

| "As a player, I want to view a list of my games so that I can see who I have played against."|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the player has to click the his/her profile button. |
|2. A menu will appear showing several options, including the "My Games" feature. |
|3. The player selects "My Games", which takes him/her to the "My Games" screen. |
|4. From there, the player can view the list of games showing information about the game number, winner and players of each game.  |

### US#13-(ISSUE#nn): View player achievements ([Issue]())

| "As a player, I want to view my achievements so that I can track my progress in the game. "|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the player has to click the his/her profile button. |
|2. A menu will appear showing several options, including the "Achievements" feature. |
|3. The player selects "Achievements", which takes him/her to the "Achievements" screen. |
|4. From there, the player can view the achievements he has earned and when, which not, and the description of each one. |

### US#14-(ISSUE#nn): View player stats ([Issue]())

| "As a player, I want to see my statistics so that I can track my performance in the game. "|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the player has to click the his/her profile button. |
|2. A menu will appear showing several options, including the "Stats" feature. |
|3. The player selects "Stats", which takes him/her to the "My Stats" screen. |
|4. From there, the player can view his/her statistics, including games played, durations, wins... |

### US#15-(ISSUE#nn): View current games ([Issue]())

| "As an administrator, I want to see a list of all ongoing games so that I can manage them and assure their good functioning. "|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the administrator has to click on his/her profile button. |
|2. A menu will appear showing several options, including the "Games" feature. |
|3. The administrator selects "Games", which takes him/her to the "Games history" screen. |
|4. A list with all ongoing games is shown, showing for each game the players that are playing it and its creator. |

### US#16-(ISSUE#nn): View past games ([Issue]())

| "As an administrator, I want to see a list of all past games so that I can manage them. "|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the administrator has to click on his/her profile button. |
|2. A menu will appear showing several options, including the "Games" feature. |
|3. The administrator selects "Games", which takes him/her to the "Games history" screen. |
|4. A list with all past games is shown, showing for each game the players that played it and its creator. |

### US#17-(ISSUE#nn): View all users ([Issue]())

| "As an administrator, I want to see a list of all registered users so that I can manage them. "|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the administrator has to click on his/her profile button. |
|2. A menu will appear showing several options, including the "Users" feature. |
|3. The administrator selects "Users", which takes him/her to the "Users" screen. |
|4. A list with all users is shown, where the administrator can see for each user the username and the authority. |

### US#18-(ISSUE#nn): Creation of user ([Issue]())

| "As an administrator, I want to create a user so that I can add a new profile to the system "|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the administrator has to click on his/her profile button. |
|2. A menu will appear showing several options, including the "Users" feature. |
|3. The administrator selects "Users", which takes him/her to the "Users" screen. |
|4. From there, the administrator clicks the "Add User" button, which takes him/her to the "Create user" screen. |
|5. From there, the administrator fills the necessary information to create the user.  |
|6. Once the administrator clicks on "Save", the user is created.  |

### US#19-(ISSUE#nn): Deletion of user ([Issue]())

| "As an administrator, I want to delete a user so that I can delete a profile from the system "|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the administrator has to click on his/her profile button. |
|2. A menu will appear showing several options, including the "Users" feature. |
|3. The administrator selects "Users", which takes him/her to the "Users" screen. |
|4. From there, the administrator can delete any user clicking on his/her "Delete" button. |
|5. After clicking "Yes" on the confirmation modal, the user will be deleted. If the administrator click "No", the user won't be deleted.  |

### US#20-(ISSUE#nn): Edition of user ([Issue]())

| "As an administrator, I want to edit a user so that I can update their information on the system "|
| ------------------------------------------------------------------------- |
|![Mockup]()|
|1. On the home screen, the administrator has to click on his/her profile button. |
|2. A menu will appear showing several options, including the "Users" feature. |
|3. The administrator selects "Users", which takes him/her to the "Users" screen. |
|4. From there, the administrator can edit any user clicking on his/her "Edit" button, taking him/her to the "Edit User" scren. |
|5. From there, the administrator updates the necessary information to edit the user.  |
|6. Once the administrator clicks on "Save", the user is updated.  |



## Diagrama conceptual del sistema
_En esta sección debe proporcionar un diagrama UML de clases que describa el modelo de datos a implementar en la aplicación. Este diagrama estará anotado con las restricciones simples (de formato/patrón, unicidad, obligatoriedad, o valores máximos y mínimos) de los datos a gestionar por la aplicación. _

_Recuerde que este es un diagrama conceptual, y por tanto no se incluyen los tipos de los atributos, ni clases específicas de librerías o frameworks, solamente los conceptos del dominio/juego que pretendemos implementar_
Ej:

```mermaid
classDiagram
    note "From Duck till Zebra"
    Animal <|-- Duck
    note for Duck "can fly\ncan swim\ncan dive\ncan help in debugging"
    Animal <|-- Fish
    Animal <|-- Zebra
    Animal : age
    Animal : gender
    class Duck{
        beakColor        
    }
    class Fish{
       sizeInFeet
    }
    class Zebra{
        is_wild
        
    }
```
_Si vuestro diagrama se vuelve demasiado complejo, siempre podéis crear varios diagramas para ilustrar todos los conceptos del dominio. Por ejemplo podríais crear un diagrama para cada uno de los módulos que quereis abordar. La única limitación es que hay que ser coherente entre unos diagramas y otros si nos referimos a las mismas clases_

_Puede usar la herramienta de modelado que desee para generar sus diagramas de clases. Para crear el diagrama anterior nosotros hemos usado un lenguaje textual y librería para la generación de diagramas llamada Mermaid_

_Si deseais usar esta herramienta para generar vuestro(s) diagramas con esta herramienta os proporcionamos un [enlace a la documentación oficial de la sintaxis de diagramas de clases de _ermaid](https://mermaid.js.org/syntax/classDiagram.html)_

## Reglas de Negocio
### R-< X > < Nombre Regla de negocio >
_< Descripción de la restricción a imponer >_

_Ej:_ 
### R1 – Diagnósticos imposibles
El diagnóstico debe estar asociado a una enfermedad que es compatible con el tipo de mascota de su visita relacionada. Por ejemplo, no podemos establecer como enfermedad diagnosticada una otitis cuando la visita está asociada a una mascota que es un pez, porque éstos no tienen orejas ni oídos (y por tanto no será uno de los tipos de mascota asociados a la enfermedad otitis en el vademecum).

…

_Muchas de las reglas del juego se transformarán en nuestro caso en reglas de negocio, por ejemplo, “la carta X solo podrá jugarse en la ronda Y si en la ronda anterior se jugó la carta Z”, o “en caso de que un jugador quede eliminado el turno cambia de sentido”_


