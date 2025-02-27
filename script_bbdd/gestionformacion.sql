DROP DATABASE IF EXISTS gestionformacion;
CREATE DATABASE gestionformacion CHARACTER SET utf8mb4;
USE gestionformacion;

CREATE TABLE rol (
	
    id BIGINT PRIMARY KEY,
    nombre_rol VARCHAR(200) NOT NULL UNIQUE,
    descripcion VARCHAR(200)
    
);

CREATE TABLE usuario (

	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	email VARCHAR(100) UNIQUE NOT NULL,
	password VARCHAR(20) NOT NULL,
    id_rol BIGINT NOT NULL,
    FOREIGN KEY(id_rol) REFERENCES rol(id)
    
);

CREATE TABLE curso (

	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(100) NOT NULL,
	descripcion VARCHAR(500) ,
	fecha_inicio TIMESTAMP NOT NULL,
	fecha_fin TIMESTAMP NOT NULL

);

CREATE TABLE curso_usuario (

	id_usuario BIGINT NOT NULL,
	id_curso BIGINT NOT NULL,
	PRIMARY KEY (id_usuario, id_curso),
	FOREIGN KEY(id_usuario) REFERENCES usuario(id),
	FOREIGN KEY(id_curso) REFERENCES curso(id)

);

INSERT INTO rol (id, nombre_rol, descripcion) VALUES (0, 'ADMIN', 'Usuario con todos los permisos'),
(1, 'ALUMNO', 'Usuario normal');

INSERT INTO usuario (id, email, password, id_rol)
 VALUES (1, 'admin@gmail.com', '1234', 0);
