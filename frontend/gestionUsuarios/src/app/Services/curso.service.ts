import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CursoService {

  private cursoApiUrl:string = 'http://localhost:8082/courses';
  private usuariosApiURl:string = 'http://localhost:8083/usuarios'

  constructor(private http: HttpClient) { }

  getCursos(): Observable<any>{
    this.getToken();
    return this.http.get(this.cursoApiUrl);
  }

  getUsuarios(usuarios: any): Observable<any> {
    const idAdmin = localStorage.getItem('idUsuario')
    const params = new HttpParams().set('idAdmin', Number(idAdmin))
    this.getToken();
    return this.http.get(`${this.usuariosApiURl}`, {params});
  }

  getCurso(id: number): Observable<any>{
    this.getToken();
    return this.http.get(`${this.cursoApiUrl}/${id}`);
  }

  createCurso(curso: any,  usuariosSeleccionados: any[]): Observable<any>{
    this.getToken();

    const idAdmin = localStorage.getItem('idUsuario')
    const params = new HttpParams().set('idAdmin', Number(idAdmin))

    curso.usuarios = usuariosSeleccionados;

    return this.http.post(this.cursoApiUrl, curso, {params});
  }

  updateCurso(id: number, curso: any,  usuariosSeleccionados: any[]): Observable<any>{
    this.getToken();
    curso.id = id;

    const idAdmin = localStorage.getItem('idUsuario')
    const params = new HttpParams().set('idAdmin', Number(idAdmin))

    curso.usuarios = usuariosSeleccionados;

    return this.http.put(`${this.cursoApiUrl}/${id}`,curso, {params})
  }

  deleteCurso(id: number): Observable<any>{
    this.getToken();

    const idAdmin = localStorage.getItem('idUsuario')
    const params = new HttpParams().set('idAdmin', Number(idAdmin))

    return this.http.delete(`${this.cursoApiUrl}/${id}`, {params});
  }

  getToken(): void{
    const token = localStorage.getItem('token');

    const header = {
      headers: new HttpHeaders({
        "Authorization": `Bearer ${token}`
      })
    }
  }
}
