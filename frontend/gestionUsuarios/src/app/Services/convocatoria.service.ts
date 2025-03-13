import { HttpClient, HttpContext, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConvocatoriaService {

  private convocatoriaApiUrl = 'http://localhost:8085/convocatorias'

  constructor(private http: HttpClient) { }

  getConvocatorias(): Observable<any> {
    const params = new HttpParams().set('idAdmin', Number(localStorage.getItem('idUsuario')))
    return this.http.get(this.convocatoriaApiUrl, {params})
  }

  getConvocatoriasActivas(): Observable<any>{
    return this.http.get(`${this.convocatoriaApiUrl}/activas`)
  }

  getConvocatoriasUsuario(): Observable<any>{
    const idUsuario = localStorage.getItem('idUsuario');
    return this.http.get(`${this.convocatoriaApiUrl}/usuario/${idUsuario}`)
  }

  createConvocatoria(convocatoria: any): Observable<any> {
    const params = new HttpParams().set('idAdmin', Number(localStorage.getItem('idUsuario')))

    console.log(convocatoria)

    return this.http.post(`${this.convocatoriaApiUrl}`, convocatoria, {params})
  }

  updateConvocatoria(convocatoria: any): Observable<any> {
    const params = new HttpParams().set('id', Number(localStorage.getItem('idUsuario')))

    const body = {
      "fechaInicio": convocatoria.fechaInicio,
      "fechaFin": convocatoria.fechaFin
    };

    return this.http.put(`${this.convocatoriaApiUrl}/${convocatoria.id}`, body, {params})
  }

  deleteConvocatoria(id: Number): Observable<any>{
    const params = new HttpParams().set('idAdmin', Number(localStorage.getItem('idUsuario')))
    
    return this.http.delete(`${this.convocatoriaApiUrl}/${id}`, {params}) 
  }

  inscribirEnConvocatoria(idConvocatoria: Number): Observable<any>{
    const params = new HttpParams()
                   .set('idAdmin',Number(localStorage.getItem('idUsuario')));
    
    return this.http.put(`${this.convocatoriaApiUrl}/${idConvocatoria}/inscribir`, params)          
  }

  enviarCertificado(idConvocatoria: Number): Observable<any>{
    const idUsuario = localStorage.getItem('idUsuario')
    const params = new HttpParams()
                   .set('idAdmin',Number(localStorage.getItem('idUsuario')));

    return this.http.post(`${this.convocatoriaApiUrl}/${idConvocatoria}/usuarios/certificado`, {params})
  }

}
