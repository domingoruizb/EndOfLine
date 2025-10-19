INSERT INTO authorities(id,authority) VALUES (1,'ADMIN');
INSERT INTO authorities(id,authority) VALUES (2,'PLAYER');
INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (1,'admin1','$2a$10$nMmTWAhPTqXqLDJTag3prumFrAJpsYtroxf0ojesFYq0k4PmcbWUS',
        1,'1990-01-15','El Super','Admin','admin1@endofline.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (4,'player1','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'2000-03-12','Liam','Torres','liam.torres@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (5,'player2','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'1999-07-25','Emma','Rodríguez','emma.rodriguez@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (6,'player3','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'2001-02-18','Noah','Gómez','noah.gomez@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (7,'player4','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'1998-10-09','Olivia','Martínez','olivia.martinez@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (8,'player5','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'2002-06-30','Elías','Navarro','elias.navarro@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (9,'player6','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'2000-12-14','Ava','Serrano','ava.serrano@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (10,'player7','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'1997-08-21','Lucas','Fernández','lucas.fernandez@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (11,'player8','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'2003-11-02','Mia','Jiménez','mia.jimenez@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (12,'player9','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'2001-05-16','Mateo','Ruiz','mateo.ruiz@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (13,'player10','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'1999-09-27','Isabella','Morales','isabella.morales@email.com');

-- Usuarios adicionales
INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (14,'RRP9465','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'1998-04-04','Makar','Lavrov','makar.lavrov@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (15,'DYS4321','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'2000-01-30','Domingo','Ruiz Bellido','domingo.ruiz@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (16,'FLX0814','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'1997-03-08','Angelo Sho','Moraschi','angelo.moraschi@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (17,'HNR0360','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'2001-09-12','Fernando Jose','Fernandez Fernandez','fernando.fernandez@email.com');

INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (18,'VMC1155','$2a$10$DaS6KIEfF5CRTFrxIoGc7emY3BpZZ0.fVjwA3NiJ.BjpGNmocaS3e',
        2,'1998-11-22','Alejandro','Urbina Tamayo','alejandro.urbina@email.com');
        
INSERT INTO users(id,username,password,authority,birthdate,name,surname,email)
VALUES (19,'Deleted user','userDeleted',
        2,'1998-11-22','User','Deleted','userDeleted@email.com');

INSERT INTO achievements(id,name,description,threshold,badge_image,category) VALUES (1,'Principiante','Si juegas 5 partidas',10.0,'https://cdn-icons-png.flaticon.com/512/5243/5243423.png','GAMES_PLAYED');
INSERT INTO achievements(id,name,description,threshold,badge_image,category) VALUES (2,'Explorador','Si juegas 25 partidas',25.0,'https://cdn-icons-png.flaticon.com/512/603/603855.png','GAMES_PLAYED');
INSERT INTO achievements(id,name,description,threshold,category) VALUES (3,'Experto','Si ganas 20 partidas',20.0,'VICTORIES');

INSERT INTO games(id,round,started_at,ended_at,host_id) VALUES (1,0, NULL, NULL,4);
INSERT INTO games(id,round,started_at,ended_at,host_id) VALUES (2,3, '2025-10-15 14:30:00', NULL,5);
INSERT INTO games(id,round,started_at,ended_at,host_id) VALUES (3,10, '2025-10-12 14:30:00', '2025-10-13 14:30:00',6);

INSERT INTO playerachievements (id, user_id, achievement_id, achieved_at) VALUES
   (1, 4, 1, '2024-02-05 14:30:00'),
   (2, 4, 2, '2024-02-05 14:30:00'),
   (3, 4, 3, '2024-02-05 14:30:00'),
   (4, 5, 1, '2024-02-18 09:15:00'),
   (5, 6, 3, '2024-01-28 13:20:00'),
   (6, 7, 2, '2024-02-25 10:50:00'),
   (7, 8, 1, '2024-03-30 16:10:00'),
   (8, 9, 3, '2024-04-08 11:20:00'),
   (9, 10, 2, '2024-04-18 09:45:00'),
   (10, 11, 1, '2024-05-02 13:30:00'),
   (11, 12, 3, '2024-05-10 15:15:00'),
   (12, 13, 2, '2024-05-20 10:05:00'),
   (13, 14, 1, '2024-05-25 12:40:00'),
   (14, 15, 3, '2024-06-01 14:55:00'),
   (15, 16, 2, '2024-06-05 16:30:00'),
   (16, 17, 1, '2024-01-20 08:20:00'),
   (17, 18, 3, '2024-02-15 11:45:00'),
   (18, 18, 2, '2024-03-20 13:10:00'),
   (19, 8, 3, '2024-03-05 09:30:00'),
   (20, 10, 1, '2024-03-15 12:25:00'),
   (21, 12, 2, '2024-04-12 14:20:00'),
   (22, 14, 3, '2024-04-25 16:45:00'),
   (23, 16, 1, '2024-05-15 10:30:00'),
   (24, 18, 1, '2024-06-10 11:15:00'),
   (25, 6, 2, '2024-02-10 13:40:00');
