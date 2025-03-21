package com.viewnext.convocatoria.business.services;

import java.util.List;

import com.viewnext.convocatoria.model.ConvocatoriaRequest;
import com.viewnext.convocatoria.model.UpdateRequest;
import com.viewnext.core.business.model.Convocatoria;

public interface ConvocatoriaServices {
	
	Convocatoria create(ConvocatoriaRequest request);
	
	List<Convocatoria> getAll();
	
	List<Convocatoria> getActivas();
	
	void update(Long id, UpdateRequest request);
	
	void delete(Long id);
	
	List<Convocatoria> getFromUsuario(Long idUsuario);
	
	void generarCertificado(Long idConvocatoria, Long idUsuario);
	
	void inscribirUsuario(Long idConvocatoria, Long idUsuario);

}
