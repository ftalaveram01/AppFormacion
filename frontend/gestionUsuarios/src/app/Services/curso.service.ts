import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CursoService {

  private cursoApiUrl: string = 'http://localhost:8102/courses';
  private usuariosApiURl: string = 'http://localhost:8103/usuarios'

  constructor(private http: HttpClient) { }

  getCursos(): Observable<any> {
    this.getToken();

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.get(this.cursoApiUrl, { headers });
  }

  getUsuarios(usuarios: any): Observable<any> {
    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.get(`${this.usuariosApiURl}`, { headers })
  }

  getCurso(id: number): Observable<any> {
    this.getToken();

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.get(`${this.cursoApiUrl}/${id}`, { headers });
  }

  createCurso(curso: any, usuariosSeleccionados: any[]): Observable<any> {
    this.getToken();

    const idAdmin = localStorage.getItem('idUsuario')
    const params = new HttpParams().set('idAdmin', Number(idAdmin))

    curso.usuarios = usuariosSeleccionados;
    curso.habilitado = 1
    curso.convocatorias = []

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.post(this.cursoApiUrl, curso, { headers, params });
  }

  updateCurso(id: number, curso: any, usuariosSeleccionados: any[]): Observable<any> {
    this.getToken();
    curso.id = id;

    curso.usuarios = usuariosSeleccionados;
    curso.habilitado = 1

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.put(`${this.cursoApiUrl}/${id}`, curso, { headers })
  }

  deleteCurso(id: number): Observable<any> {
    this.getToken();

    const idAdmin = localStorage.getItem('idUsuario')
    const params = new HttpParams().set('idAdmin', Number(idAdmin))

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.delete(`${this.cursoApiUrl}/${id}`, { headers, params });
  }

  addUsuarioToCurso(idCurso: number, idUsuario: number): Observable<any> {
    const params = new HttpParams().set('idUsuario', idUsuario).set('idCurso', idCurso)

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.put(`${this.cursoApiUrl}`, params, { headers });
  }

  deleteUsuarioFromCurso(idUsuario: number, idCurso: number): Observable<any> {
    const params = new HttpParams()
      .set('idUsuario', idUsuario)
      .set('idCurso', idCurso)

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.delete(`${this.cursoApiUrl}`, { headers, params });
  }

  getToken(): void {
    const token = localStorage.getItem('token');

    const header = {
      headers: new HttpHeaders({
        "Authorization": `Bearer ${token}`
      })
    }
  }
}
