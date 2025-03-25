package com.viewnext.convocatoria.business.services;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Convocatoria;
import com.viewnext.core.business.model.ConvocatoriaEnum;
import com.viewnext.core.repositories.ConvocatoriaRepository;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;

@Service
public class ConvocatoriaScheduler {
    
    private ScheduledExecutorService scheduledExecutorService;
    
    private ConvocatoriaRepository convocatoriaRepository;
    
    private Map<Convocatoria, List<ScheduledFuture<?>>> tareasProgramadas = new ConcurrentHashMap<>();
    
    public ConvocatoriaScheduler(ScheduledExecutorService scheduledExecutorService,
            ConvocatoriaRepository convocatoriaRepository) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.convocatoriaRepository = convocatoriaRepository;
    }
    
    @PostConstruct
    public void init() {
        reiniciarScheduler();
    }
    
    @PreDestroy
    public void destroy() {
        for (List<ScheduledFuture<?>> tareas : tareasProgramadas.values()) {
            for (ScheduledFuture<?> tarea : tareas) {
                tarea.cancel(true);
            }
        }
        tareasProgramadas.clear();
    }

    public void programarTarea(Convocatoria convocatoria, boolean crearModificar, boolean postConstruct) {
    	 Date fechaInicio = convocatoria.getFechaInicio();
         Date fechaFin = convocatoria.getFechaFin();
    	
        if (crearModificar && !postConstruct) {
            
            ScheduledFuture<?> tareaInicio = scheduledExecutorService.schedule(() -> 
                tareaFechaInicio(convocatoria)
            , fechaInicio.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            
            ScheduledFuture<?> tareaFin = scheduledExecutorService.schedule(() -> 
                tareaFechaFin(convocatoria)
            , fechaFin.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            
            tareasProgramadas.put(convocatoria, new ArrayList<>());
            List<ScheduledFuture<?>> tareas = tareasProgramadas.get(convocatoria);
            tareas.add(tareaInicio);
            tareas.add(tareaFin);

        } else if(!postConstruct) {
            cancelarTareas(convocatoria);
        }else {
        	
        	if(fechaInicio.after(new Date()))
        		programarTarea(convocatoria, true, false);
        	else {
        		
        		if(convocatoria.getEstado().equals(ConvocatoriaEnum.EN_PREPARACION) || convocatoria.getEstado().equals(ConvocatoriaEnum.CONVOCADA)) {
        			tareaFechaInicio(convocatoria);
        		}
        		
        		if(!convocatoria.getEstado().equals(ConvocatoriaEnum.DESIERTA)) {
        			
            		if(fechaFin.before(new Date()))
            			tareaFechaFin(convocatoria);
            		else {
            			
                        ScheduledFuture<?> tareaFin = scheduledExecutorService.schedule(() -> 
                            tareaFechaFin(convocatoria)
                        , fechaFin.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                        tareasProgramadas.put(convocatoria, new ArrayList<>());
                        List<ScheduledFuture<?>> tareas = tareasProgramadas.get(convocatoria);
                        tareas.add(tareaFin);
                        
            		}
        		}

        	}
        }
    }
    
    public void cancelarTareas(Convocatoria convocatoria) {
        List<ScheduledFuture<?>> tareas = tareasProgramadas.get(convocatoria);
        if (tareas != null) {
            for (ScheduledFuture<?> tarea : tareas) {
                tarea.cancel(true);
            }
            tareasProgramadas.remove(convocatoria);
        }
    }
    
    @Transactional
    public void tareaFechaInicio(Convocatoria convocatoria) {
        Convocatoria actualizada = convocatoriaRepository.findById(convocatoria.getId()).get();
        if(actualizada.getUsuarios().size()>9) {
        	actualizada.setEstado(ConvocatoriaEnum.EN_CURSO);
        	convocatoria.setEstado(ConvocatoriaEnum.EN_CURSO);
        }else {
            actualizada.setEstado(ConvocatoriaEnum.DESIERTA);
            convocatoria.setEstado(ConvocatoriaEnum.DESIERTA);
            cancelarTareas(convocatoria);
        }
        convocatoriaRepository.save(actualizada);
        
    }
    
    @Transactional
    public void tareaFechaFin(Convocatoria convocatoria) {
    	Convocatoria actualizada = convocatoriaRepository.findById(convocatoria.getId()).get();
    	actualizada.setEstado(ConvocatoriaEnum.TERMINADA);
    	convocatoriaRepository.save(actualizada);
    }
    
    public void reiniciarScheduler() {
        tareasProgramadas.clear();
        List<Convocatoria> convocatorias = convocatoriaRepository.findAll();
        for (Convocatoria convocatoria : convocatorias) {
            if (!convocatoria.getEstado().equals(ConvocatoriaEnum.DESIERTA) && !convocatoria.getEstado().equals(ConvocatoriaEnum.TERMINADA)) {
                programarTarea(convocatoria, true, true);
            }
        }
    }


}
