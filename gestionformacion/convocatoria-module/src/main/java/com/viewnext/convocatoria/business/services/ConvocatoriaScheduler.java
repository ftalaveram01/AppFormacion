package com.viewnext.convocatoria.business.services;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viewnext.core.business.model.Convocatoria;

@Service
public class ConvocatoriaScheduler {
	
	@Autowired
	private ScheduledExecutorService scheduledExecutorService;
	
	private Map<Convocatoria, ScheduledFuture<?>> tareasProgramadas = new ConcurrentHashMap<>();
	
    public void programarTarea(Convocatoria convocatoria, boolean crearModificar) {
        if (crearModificar) {
            // Programar tareas para la convocatoria
            Date fechaInicio = convocatoria.getFechaInicio();
            Date fechaFin = convocatoria.getFechaFin();
            // Programar tarea para la fecha de inicio
            ScheduledFuture<?> tareaInicio = scheduledExecutorService.schedule(() -> {
                // Ejecutar tarea para la fecha de inicio
            }, fechaInicio.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            tareasProgramadas.put(convocatoria, tareaInicio);
            // Programar tarea para la fecha de fin
            ScheduledFuture<?> tareaFin = scheduledExecutorService.schedule(() -> {
                // Ejecutar tarea para la fecha de fin
            }, fechaFin.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            tareasProgramadas.put(convocatoria, tareaFin);
        } else {
            // Cancelar tareas para la convocatoria
            cancelarTareas(convocatoria);
        }
    }
    public void cancelarTareas(Convocatoria convocatoria) {
        ScheduledFuture<?> tarea = tareasProgramadas.get(convocatoria);
        if (tarea != null) {
            tarea.cancel(true);
            tareasProgramadas.remove(convocatoria);
        }
    }

}
