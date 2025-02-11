package com.viewnext.course.business.model;

import java.util.Date;
import java.util.List;

import com.viewnext.login.business.model.Usuario;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
public class Course {

	@Id
	private Long id;
	
	private String nombre;
	private String descripcion;
	
	@ElementCollection
	@JoinTable(name="usuario_curso", 
					joinColumns = @JoinColumn(name="id_curso"))
	private List<Usuario> listaMiembros;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaFin; 
	
}
