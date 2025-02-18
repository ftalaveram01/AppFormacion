DROP DATABASE IF EXISTS gestionformacion;
CREATE DATABASE gestionformacion CHARACTER SET utf8mb4;
USE gestionformacion;

CREATE TABLE rol (
	
    nombre_rol VARCHAR(200) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    PRIMARY KEY(nombre_rol)
    
);

CREATE TABLE usuario (

	id INT AUTO_INCREMENT PRIMARY KEY,
	email VARCHAR(100) UNIQUE NOT NULL,
	password VARCHAR(20) NOT NULL,
    rol VARCHAR(200) NOT NULL,
    FOREIGN KEY(rol) REFERENCES rol(nombre_rol)
    
);

CREATE TABLE curso (

	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(100) NOT NULL,
	descripcion VARCHAR(500) ,
	fecha_inicio TIMESTAMP NOT NULL,
	fecha_fin TIMESTAMP NOT NULL

);

INSERT INTO rol (nombre_rol, descripcion) VALUES ('ADMIN', 'Usuario con todos los permisos'),
('ALUMNO', 'Usuario normal');
