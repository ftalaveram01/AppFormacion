USE gestionformacion;
CREATE TABLE Rol (
    id BIGINT PRIMARY KEY,
    nombre_rol VARCHAR(200) NOT NULL UNIQUE,
    descripcion VARCHAR(200)
);

CREATE TABLE Usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    secreto VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    id_rol BIGINT,
    habilitado BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY(id_rol) REFERENCES Rol(id) ON DELETE RESTRICT
);

CREATE TABLE Curso (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    numero_horas INTEGER NOT NULL,
    habilitado BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE Curso_Usuario (
    id_usuario BIGINT NOT NULL,
    id_curso BIGINT NOT NULL,
    PRIMARY KEY (id_usuario, id_curso),
    FOREIGN KEY(id_usuario) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY(id_curso) REFERENCES Curso(id) ON DELETE CASCADE
);

CREATE TABLE Convocatoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin TIMESTAMP NOT NULL,
    estado VARCHAR(200) NOT NULL,
    id_curso BIGINT NOT NULL,
    FOREIGN KEY(id_curso) REFERENCES Curso(id)
);

CREATE TABLE Convocatoria_Usuario (
    id_usuario BIGINT NOT NULL,
    id_convocatoria BIGINT NOT NULL,
    PRIMARY KEY (id_usuario, id_convocatoria),
    FOREIGN KEY(id_usuario) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY(id_convocatoria) REFERENCES Convocatoria(id) ON DELETE CASCADE
);

INSERT INTO Rol (id, nombre_rol, descripcion) VALUES
(0, 'ADMIN', 'Usuario con todos los permisos'),
(1, 'ALUMNO', 'Usuario normal');

INSERT INTO Curso (id, nombre, descripcion, numero_horas, habilitado) VALUES
(1, 'Curso de Angular', 'Curso de introducción a Angular', 200, true),
(2, 'Curso de React', 'Curso de introducción a React', 60, true),
(3, 'Curso de Vue.js', 'Curso de introducción a Vue.js', 100, false);