package com.viewnext.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.viewnext.core.business.model.Convocatoria;
import com.viewnext.core.business.model.ConvocatoriaReporte;

@Repository
public interface ConvocatoriaRepository extends JpaRepository<Convocatoria, Long> {

	
    @Query("SELECT new com.viewnext.core.business.model.ConvocatoriaReporte(c.id, c.fechaInicio, c.fechaFin, CONCAT(c.estado,''), c.curso.nombre, SIZE(c.usuarios)) " +
            "FROM Convocatoria c")
     List<ConvocatoriaReporte> findAllConvocatoriaReportes();
}
