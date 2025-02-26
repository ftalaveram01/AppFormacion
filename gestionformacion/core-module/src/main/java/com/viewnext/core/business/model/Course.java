package com.viewnext.core.business.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "curso")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	private String descripcion;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_inicio", nullable = false)
	private Date fechaInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_fin", nullable = false)
	private Date fechaFin;
	
	@ManyToMany
	@JoinTable(
	    name = "curso_usuario",
	    joinColumns = @JoinColumn(name = "id_curso"),
	    inverseJoinColumns = @JoinColumn(name = "id_usuario"))
	private List<Usuario> usuarios;
	
}
