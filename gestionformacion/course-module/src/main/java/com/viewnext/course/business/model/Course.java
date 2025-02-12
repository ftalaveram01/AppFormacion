package com.viewnext.course.business.model;

import java.util.Date;
import java.util.List;
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

@Data
@Entity
@Table(name = "curso")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	private String descripcion;
	
	@ManyToMany
	@JoinTable(name="usuario_curso", 
					joinColumns = @JoinColumn(name="id_curso"),
					inverseJoinColumns = @JoinColumn(name="id_usuario"))
	private List<Usuario> listaMiembros;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaFin; 
	
}
