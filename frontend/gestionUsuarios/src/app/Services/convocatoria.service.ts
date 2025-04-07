import { HttpClient, HttpContext, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConvocatoriaService {

  private convocatoriaApiUrl = 'http://localhost:8105/convocatorias'

  constructor(private http: HttpClient) { }

  getConvocatorias(): Observable<any> {
    const params = new HttpParams().set('idAdmin', Number(localStorage.getItem('idUsuario')))

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.get(this.convocatoriaApiUrl, { headers, params })
  }

  getConvocatoriasActivas(): Observable<any> {
    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });
    return this.http.get(`${this.convocatoriaApiUrl}/activas`, { headers })
  }

  getConvocatoriasUsuario(): Observable<any> {
    const idUsuario = localStorage.getItem('idUsuario');
    return this.http.get(`${this.convocatoriaApiUrl}/usuario/${idUsuario}`)
  }

  createConvocatoria(convocatoria: any): Observable<any> {
    const params = new HttpParams().set('idAdmin', Number(localStorage.getItem('idUsuario')))

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.post(`${this.convocatoriaApiUrl}`, convocatoria, { headers, params })
  }

  updateConvocatoria(id: Number, convocatoriaActualizada: any): Observable<any> {
    const params = new HttpParams().set('idAdmin', Number(localStorage.getItem('idUsuario')))

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.put(`${this.convocatoriaApiUrl}/${id}`, convocatoriaActualizada, { headers, params })
  }

  deleteConvocatoria(id: Number): Observable<any> {
    const params = new HttpParams().set('idAdmin', Number(localStorage.getItem('idUsuario')))

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.delete(`${this.convocatoriaApiUrl}/${id}`, { headers, params })
  }

  inscribirEnConvocatoria(idConvocatoria: Number, idUsuario: Number): Observable<any> {
    const params = new HttpParams().set('idUsuario', String(idUsuario))

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.put(`${this.convocatoriaApiUrl}/${idConvocatoria}/inscribir`, params, { headers })
  }

  enviarCertificado(idConvocatoria: Number): Observable<any> {
    const idUsuario = localStorage.getItem('idUsuario')
    const params = new HttpParams()
      .set('idAdmin', Number(localStorage.getItem('idUsuario')));

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.post(`${this.convocatoriaApiUrl}/${idConvocatoria}/usuarios/certificado`, { headers, params })
  }

}
