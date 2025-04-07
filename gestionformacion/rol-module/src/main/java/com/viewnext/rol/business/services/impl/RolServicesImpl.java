package com.viewnext.rol.business.services.impl;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.repositories.RolRepository;
import com.viewnext.rol.business.services.RolServices;

import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class RolServicesImpl implements RolServices{
	
	private RolRepository rolRepository;
	
	public RolServicesImpl(RolRepository rolRepository) {
		this.rolRepository = rolRepository;
	}

	@Transactional
	@Override
	public Rol create(Rol rol) {
		
		if(rol.getId() == null)
			throw new IllegalStateException("El rol tiene que tener un id valido.");
		
		if(rolRepository.existsById(rol.getId()))
			throw new IllegalStateException("Ya existe un rol con ese id.");
		
		return rolRepository.save(rol);
	}

	@Transactional
	@Override
	public void delete(Long id) {
	    
	    if(!rolRepository.existsById(id))
	        throw new IllegalStateException("Rol no encontrado");
	    
	    rolRepository.deleteById(id);
	}

	@Transactional
	@Override
	public Rol update(String descripcion, Long id) {
		
		if(!rolRepository.existsById(id))
			throw new IllegalStateException("El rol con ID [" + id + "] no existe.");
		
		Rol rol = rolRepository.findById(id).get();
		rol.setDescripcion(descripcion);
		
		return rolRepository.save(rol);
	}

	@Override
	public List<Rol> getAll() {
		return rolRepository.findAll();
	}

	@Override
	public Rol read(Long id) {
		if(!rolRepository.existsById(id))
			throw new IllegalStateException("El rol con ID [" + id + "] no existe.");
		return rolRepository.findById(id).get();
	}

	@Override
	public byte[] generarReporte() {
		List<Rol> roles = rolRepository.findAll();

	    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(roles);

	    JasperReport jasperReport = null;
	    JasperPrint jasperPrint = null;
	    byte[] resultado = null;

	    InputStream reportStream = getClass().getResourceAsStream("/rol.jrxml");
	    try {
	        jasperReport = JasperCompileManager.compileReport(reportStream);
	    } catch (JRException e) {
	        throw new IllegalStateException("Error al generar el reporte");
	    }

	    Map<String, Object> parameters = new HashMap<>();
	    parameters.put("ReportTitle", "Reporte de Roles");

	    try {
	        jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
	        resultado = JasperExportManager.exportReportToPdf(jasperPrint);
	    } catch (JRException e) {
	        throw new IllegalStateException("Error al generar el reporte");
	    }

	    try (FileOutputStream fos = new FileOutputStream("ReporteRol.pdf")) {
	        fos.write(resultado);
	    } catch (IOException e) {
	        System.out.println("Error saving PDF to file: " + e.getMessage());
	    }

	    return resultado;
	}

}
