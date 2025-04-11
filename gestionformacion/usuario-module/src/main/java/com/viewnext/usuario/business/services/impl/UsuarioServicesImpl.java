package com.viewnext.usuario.business.services.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Rol;
import com.viewnext.core.business.model.Usuario;
import com.viewnext.core.business.model.UsuarioReporte;
import com.viewnext.core.repositories.RolRepository;
import com.viewnext.core.repositories.UsuarioRepository;
import com.viewnext.usuario.business.services.UsuarioServices;

import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class UsuarioServicesImpl implements UsuarioServices{
	
	private UsuarioRepository usuarioRepository;
	
	private RolRepository rolRepository;
	
	private PasswordEncoder passwordEncoder;

	public UsuarioServicesImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository, PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.rolRepository = rolRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	@Override
	public Long create(Usuario usuario) {
		
		if(usuario.getId()!=null)
			throw new IllegalStateException("El usuario no puede tener id");
		if(Boolean.TRUE.equals(usuarioRepository.existsByEmail(usuario.getEmail())))
			throw new IllegalStateException("Ya existe un usuario con ese email.");
		
		if(usuario.getRol().getId() == null) {
			
			Rol rol = rolRepository.findByNombreRol(usuario.getRol().getNombreRol());
			if(rol == null)
				throw new IllegalStateException("No existe el rol del usuario.");
			
			usuario.setRol(rol);
			
		}
		
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		
		Usuario creado = usuarioRepository.save(usuario);
		
		return creado.getId();
	}

	@Transactional
	@Override
	public void delete(Long id) {
		
		if(!usuarioRepository.existsById(id))
			throw new IllegalStateException("No existe el usuario a borrar.");
		
		Usuario usu = usuarioRepository.findById(id).get();
		
		if(Boolean.TRUE.equals(usu.getHabilitado())) {
			usu.setHabilitado(false);
			usuarioRepository.save(usu);
		}else
			throw new IllegalStateException("El usuario ya está deshabilitado");
	}

	@Transactional
	@Override
	public void update(Usuario usuario) {
		
		if(!usuarioRepository.existsById(usuario.getId()))
			throw new IllegalStateException("No existe el usuario a actualizar.");
		
		if(usuario.getPassword().equals("")) {
			usuario.setPassword(usuarioRepository.findById(usuario.getId()).get().getPassword());
		}else {
			usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		}
		
		usuarioRepository.save(usuario);
	}
	
	@Transactional
	@Override
	public void habilitar(Long id) {

		if(!usuarioRepository.existsById(id))
			throw new IllegalStateException("No existe el usuario a actualizar.");
		
		Usuario usu = usuarioRepository.findById(id).get();
		if(usu.getHabilitado())
			throw new IllegalStateException("El usuario ya está habilitado");
		usu.setHabilitado(true);
		
		usuarioRepository.save(usu);
	}
	
	@Transactional
	@Override
	public void deshabilitarUsuario(String email) {
		
		if(!usuarioRepository.existsByEmail(email))
			throw new IllegalStateException("No existe el usuario.");
		
		Usuario usu = usuarioRepository.findByEmail(email);
		
		if(Boolean.FALSE.equals(usu.getHabilitado()))
			throw new IllegalStateException("El usuario ya está deshabilitado");
		usu.setHabilitado(false);
		
		usuarioRepository.save(usu);
		
	}

	@Override
	public List<Usuario> getAll() {
		
		return usuarioRepository.findAll();
	}

	@Override
	public Usuario read(Long id) {
		
		Optional<Usuario> usu = usuarioRepository.findById(id);
		
		if(usu.isEmpty())
			throw new IllegalStateException("No existe el usuario");
		
		return usu.get();
	}

	@Override
	public byte[] generarReporte() {
		List<UsuarioReporte> usuarios = usuarioRepository.findAllUsuarioReportes();
		
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(usuarios);

	    JasperReport jasperReport = null;
	    JasperPrint jasperPrint = null;
	    byte[] resultado = null;

	    InputStream reportStream = getClass().getResourceAsStream("/usuario.jrxml");
	    try {
	        jasperReport = JasperCompileManager.compileReport(reportStream);
	    } catch (JRException e) {
	        throw new IllegalStateException("Error al generar el reporte");
	    }

	    Map<String, Object> parameters = new HashMap<>();
	    parameters.put("ReportTitle", "Reporte de Usuarios");

	    try {
	        jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
	        resultado = JasperExportManager.exportReportToPdf(jasperPrint);
	    } catch (JRException e) {
	        throw new IllegalStateException("Error al generar el reporte");
	    }

	    try (FileOutputStream fos = new FileOutputStream("ReporteUsuarios.pdf")) {
	        fos.write(resultado);
	    } catch (IOException e) {
	        System.out.println("Error saving PDF to file: " + e.getMessage());
	    }

	    return resultado;
	}

}
