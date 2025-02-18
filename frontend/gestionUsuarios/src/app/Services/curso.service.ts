import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CursoService {

  private cursoApiUrl:string = 'http://localhost:8082/courses';

  constructor(private http: HttpClient) { }

  getCursos(): Observable<any>{
    this.getToken();
    return this.http.get(this.cursoApiUrl);
  }

  getCurso(id: number): Observable<any>{
    this.getToken();
    return this.http.get(`${this.cursoApiUrl}/${id}`);
  }

  createCurso(curso: any): Observable<any>{
    this.getToken();
    return this.http.post(this.cursoApiUrl, curso);
  }

  updateCurso(id: number, curso: any): Observable<any>{
    this.getToken();
    curso.id = id;
    return this.http.put(`${this.cursoApiUrl}`,curso)
  }

  deleteCurso(id: number): Observable<any>{
    this.getToken();
    return this.http.delete(`${this.cursoApiUrl}/${id}`);
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
