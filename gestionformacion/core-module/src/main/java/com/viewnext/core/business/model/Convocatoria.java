package com.viewnext.core.business.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
@Entity
public class Convocatoria {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_inicio", nullable = false)
	private Date fechaInicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_fin", nullable = false)
	private Date fechaFin;
	
	@Enumerated(EnumType.STRING)
	private ConvocatoriaEnum estado;
	
    @ManyToOne
    @JoinColumn(name = "id_curso")
    @JsonIgnoreProperties("convocatorias")
    private Course curso;
	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "convocatoria_usuario",
        joinColumns = @JoinColumn(name = "id_convocatoria"),
        inverseJoinColumns = @JoinColumn(name = "id_usuario"))
    private List<Usuario> usuarios;


}
