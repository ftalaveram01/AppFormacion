import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RolService {

  private rolApiUrl:string = 'http://localhost:8084/roles';

  constructor(private http: HttpClient) { }

  getRoles(): Observable<any>{
    this.getToken();

    return this.http.get(`${this.rolApiUrl}`)
  }

  getRolById(idRol: number): Observable<any>{
    return this.http.get(`${this.rolApiUrl}/${idRol}`)
  }

  createRol(rol: any): Observable<any>{
    this.getToken();

    const idAdmin = localStorage.getItem('idUsuario')
    const params = new HttpParams().set('idAdmin', Number(idAdmin))

    return this.http.post(this.rolApiUrl, rol, {params});
  }

  updateRol(id: number, rol: any): Observable<any>{
    this.getToken();

    rol.id = id

    const idAdmin = localStorage.getItem("idUsuario")
    const params = new HttpParams().set('idAdmin', Number(idAdmin))

    console.log(idAdmin)

    return this.http.put(`${this.rolApiUrl}/${id}`,rol, {params})
  }

  deleteRol(id: number): Observable<any>{
    this.getToken();

    const idAdmin = localStorage.getItem('idUsuario')
    const params = new HttpParams().set('idAdmin', Number(idAdmin))

    return this.http.delete(`${this.rolApiUrl}/${id}`, {params});
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
