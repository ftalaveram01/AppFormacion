package com.viewnext.convocatoria.business.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.viewnext.convocatoria.business.services.ConvocatoriaScheduler;
import com.viewnext.convocatoria.business.services.ConvocatoriaServices;
import com.viewnext.convocatoria.integration.repositories.ConvocatoriaRepository;
import com.viewnext.convocatoria.model.ConvocatoriaRequest;
import com.viewnext.convocatoria.model.UpdateRequest;
import com.viewnext.core.business.model.Convocatoria;

@Service
public class ConvocatoriaServicesImpl implements ConvocatoriaServices {
	
	private ConvocatoriaRepository convocatoriaRepository;
	
	private ConvocatoriaScheduler convocatoriaScheduler;
	
	public ConvocatoriaServicesImpl(ConvocatoriaRepository convocatoriaRepository,
			ConvocatoriaScheduler convocatoriaScheduler) {
		this.convocatoriaRepository = convocatoriaRepository;
		this.convocatoriaScheduler = convocatoriaScheduler;
	}

	@Override
	public Convocatoria create(Long idAdmin, ConvocatoriaRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Convocatoria> getAll(Long idAdmin) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Convocatoria> getActivas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Long id, Long idAdmin, UpdateRequest request) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Long id, Long idAdmin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Convocatoria> getFromUsuario(Long idUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void generarCertificado(Long idConvocatoria, Long idUsuario) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inscribirUsuario(Long idConvocatoria, Long idUsuario) {
		// TODO Auto-generated method stub
		
	}

}
