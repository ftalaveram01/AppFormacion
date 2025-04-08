package com.viewnext.core.business.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
@Entity
@Table(name = "Rol")
public class Rol implements Serializable{
	private static final long serialVersionUID = 2L;
	
	@Id
	Long id;
	
	@Column(name = "nombre_rol")
	private String nombreRol;
	
	private String descripcion;

}
