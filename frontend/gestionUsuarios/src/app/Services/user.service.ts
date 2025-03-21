import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userApiUrl = 'http://localhost:8103/usuarios';

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<any> {
    this.getToken();

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.get(`${this.userApiUrl}`, { headers })
  }

  getUser(id: number, idAdmin: number): Observable<any> {
    this.getToken();

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.get(`${this.userApiUrl}/${id}`, { headers })
  }

  createUser(user: any): Observable<any> {
    this.getToken();

    user.habilitado = 1

    const idAdmin = localStorage.getItem("idUsuario")

    const objetoRol: any = {
      id: Number(user.rol)
    }
    user.rol = objetoRol

    const params = new HttpParams().set('idAdmin', Number(idAdmin));

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.post(`${this.userApiUrl}/crear`, user, { headers, params })
  }

  updateUser(id: number, user: any): Observable<any> {
    this.getToken();

    console.log("SE HA METIDO EN UPDATE")

    const idAdmin = localStorage.getItem("idUsuario")

    user.id = id

    const objetoRol: any = {
      id: Number(user.rol)
    }
    user.rol = objetoRol
    user.habilitado = 1

    console.log(idAdmin)

    const params = new HttpParams()
      .set('idAdmin', Number(idAdmin));

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.put(`${this.userApiUrl}/actualizar`, user, { headers, params })
  }

  deleteUser(id: number) {
    this.getToken();

    const idAdmin = localStorage.getItem("idUsuario")

    const params = new HttpParams()
      .set('idAdmin', Number(idAdmin));

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.delete(`${this.userApiUrl}/borrar/${id}`, { headers, params })
  }

  deshabilitar(email: string, callback: (ok: boolean, error?: any) => void) {
    this.getToken();

    const params = new HttpParams()
      .set('email', email);

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const headers = new HttpHeaders({
      "Authorization": `Bearer ${token}`
    });

    return this.http.delete(`${this.userApiUrl}/deshabilitar`, { headers, params }).subscribe(
      response => {
        callback(true, 'El usuario fue bloqueado')
      },
      error => {
        if (error.status == 400) {
          callback(false, error.error)
        }
      });
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
