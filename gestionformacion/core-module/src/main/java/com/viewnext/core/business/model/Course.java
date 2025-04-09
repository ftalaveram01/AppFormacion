package com.viewnext.core.business.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "Curso")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	private String descripcion;
	
	@Column(name = "numero_horas")
	private Integer numeroHoras;
	
	@ManyToMany
	@JoinTable(
	    name = "Curso_Usuario",
	    joinColumns = @JoinColumn(name = "id_curso"),
	    inverseJoinColumns = @JoinColumn(name = "id_usuario"))
	private List<Usuario> usuarios;
	
	@OneToMany(mappedBy = "curso")
	@JsonIgnoreProperties("curso")
	private List<Convocatoria> convocatorias;

	
	private Boolean habilitado;
	
}
