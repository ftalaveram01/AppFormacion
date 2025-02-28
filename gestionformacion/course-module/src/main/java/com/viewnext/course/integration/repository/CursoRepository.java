package com.viewnext.course.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.viewnext.core.business.model.Course;

import jakarta.transaction.Transactional;

@Repository
public interface CursoRepository extends JpaRepository<Course, Long>{

    @Modifying
    @Transactional
    @Query("UPDATE Course c SET c.usuarios = (SELECT u FROM c.usuarios u WHERE u.id != :idUsuario) WHERE c.id = :idCurso")
    void deleteUsuarioFromCurso(@Param("usuarioId") Long idUsuario, @Param("cursoId") Long idCurso);
}
