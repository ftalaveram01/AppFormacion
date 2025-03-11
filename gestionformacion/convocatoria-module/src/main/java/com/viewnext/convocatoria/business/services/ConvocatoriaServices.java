package com.viewnext.convocatoria.business.services;

import java.util.List;

import com.viewnext.convocatoria.model.ConvocatoriaRequest;
import com.viewnext.convocatoria.model.UpdateRequest;
import com.viewnext.core.business.model.Convocatoria;

public interface ConvocatoriaServices {
	
	Convocatoria create(Long idAdmin, ConvocatoriaRequest request);
	
	List<Convocatoria> getAll(Long idAdmin);
	
	List<Convocatoria> getActivas();
	
	void update(Long id, Long idAdmin, UpdateRequest request);
	
	void delete(Long id, Long idAdmin);
	
	List<Convocatoria> getFromUsuario(Long idUsuario);
	
	void generarCertificado(Long idConvocatoria, Long idUsuario);
	
	void inscribirUsuario(Long idConvocatoria, Long idUsuario);

}
