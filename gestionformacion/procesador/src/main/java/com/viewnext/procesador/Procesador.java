package com.viewnext.procesador;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;

@AutoService(javax.annotation.processing.Processor.class)
@SupportedAnnotationTypes("com.viewnext.procesador.HolaMundo")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class Procesador extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    	Messager messager = processingEnv.getMessager();
    	System.out.println("Hola Mundo");
    	
    	for(Element element : roundEnv.getElementsAnnotatedWith(HolaMundo.class)) {
			System.out.println("Hola Mundo");
		}

        return true;
    }
}
