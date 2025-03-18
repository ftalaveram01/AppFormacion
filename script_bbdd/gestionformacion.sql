DROP DATABASE IF EXISTS gestionformacion;
CREATE DATABASE gestionformacion CHARACTER SET utf8mb4;
USE gestionformacion;

CREATE TABLE Rol (
    id BIGINT PRIMARY KEY,
    nombre_rol VARCHAR(200) NOT NULL UNIQUE,
    descripcion VARCHAR(200)
);

CREATE TABLE Usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(20) NOT NULL,
    id_Rol BIGINT,
    habilitado BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY(id_Rol) REFERENCES Rol(id) ON DELETE RESTRICT
);

CREATE TABLE Curso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    numero_horas INTEGER NOT NULL,
    habilitado BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Curso_Usuario (
    id_Usuario BIGINT NOT NULL,
    id_Curso BIGINT NOT NULL,
    PRIMARY KEY (id_Usuario, id_Curso),
    FOREIGN KEY(id_Usuario) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY(id_Curso) REFERENCES Curso(id) ON DELETE CASCADE
);

CREATE TABLE Convocatoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin TIMESTAMP NOT NULL,
    estado VARCHAR(200) NOT NULL,
    id_Curso BIGINT NOT NULL,
    FOREIGN KEY(id_Curso) REFERENCES Curso(id)
);

CREATE TABLE Convocatoria_Usuario (
    id_Usuario BIGINT NOT NULL,
    id_Convocatoria BIGINT NOT NULL,
    PRIMARY KEY (id_Usuario, id_Convocatoria),
    FOREIGN KEY(id_Usuario) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY(id_Convocatoria) REFERENCES Convocatoria(id) ON DELETE CASCADE
);

INSERT INTO Rol (id, nombre_rol, descripcion) VALUES
(0, 'ADMIN', 'Usuario con todos los permisos'),
(1, 'ALUMNO', 'Usuario normal');

INSERT INTO Usuario (id, email, password, id_Rol, habilitado) VALUES
(1, 'admin@gmail.com', '1234', 0, TRUE),
(2, 'alumno1@example.com', '1234', 1, TRUE),
(3, 'alumno2@example.com', '1234', 1, TRUE),
(4, 'alumno3@example.com', '1234', 1, TRUE),
(5, 'alumno4@example.com', '1234', 1, FALSE);

INSERT INTO Curso (id, nombre, descripcion, fecha_inicio, fecha_fin) VALUES
(1, 'Curso de Angular', 'Curso de introducción a Angular', '2023-01-01 00:00:00', '2023-01-31 23:59:59'),
(2, 'Curso de React', 'Curso de introducción a React', '2023-02-01 00:00:00', '2023-02-28 23:59:59'),
(3, 'Curso de Vue.js', 'Curso de introducción a Vue.js', '2023-03-01 00:00:00', '2023-03-31 23:59:59');

INSERT INTO Curso_Usuario (id_Usuario, id_Curso) VALUES
(2, 1),
(2, 2),
(3, 1),
(3, 3),
(4, 2),
(4, 3),
(5, 1);