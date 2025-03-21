import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RolService {

  private rolApiUrl: string = 'http://localhost:8104/roles';

  constructor(private http: HttpClient) { }

  getRoles(): Observable<any> {
    this.getToken();

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.get(`${this.rolApiUrl}`, { headers })
  }

  getRolById(idRol: number): Observable<any> {

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.get(`${this.rolApiUrl}/${idRol}`, { headers })
  }

  createRol(rol: any): Observable<any> {
    this.getToken();

    const idAdmin = localStorage.getItem('idUsuario')
    const params = new HttpParams().set('idAdmin', Number(idAdmin))

    rol.id = this.generarIdAleatoria()

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.post(this.rolApiUrl, rol, { headers, params });
  }

  updateRol(id: number, rol: any): Observable<any> {
    this.getToken();

    rol.id = id

    const idAdmin = localStorage.getItem("idUsuario")
    const params = new HttpParams().set('idAdmin', Number(idAdmin))

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.put(`${this.rolApiUrl}/${id}`, rol.descripcion, { headers, params })
  }

  deleteRol(id: number): Observable<any> {
    this.getToken();

    const idAdmin = localStorage.getItem('idUsuario')
    const params = new HttpParams().set('idAdmin', Number(idAdmin))

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.delete(`${this.rolApiUrl}/${id}`, { headers, params });
  }

  getToken(): void {
    const token = localStorage.getItem('token');

    const header = {
      headers: new HttpHeaders({
        "Authorization": `Bearer ${token}`
      })
    }
  }

  private generarIdAleatoria() {
    return Math.floor(Math.random() * (Number.MAX_SAFE_INTEGER - 2 + 1)) + 2;
  }

}
