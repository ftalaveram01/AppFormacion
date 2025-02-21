import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userApiUrl = 'http://localhost:8083/usuarios';

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<any>{
    this.getToken();

    const idAdmin = localStorage.getItem("idUsuario")

    const params = new HttpParams()
                    .set('idAdmin', Number(idAdmin));

    return this.http.get(`${this.userApiUrl}`, {params})
  }

  getUser(id:number, idAdmin:number): Observable<any>{
    this.getToken();

    const params = new HttpParams()
    .set('idAdmin', id)

    return this.http.get(`${this.userApiUrl}/${id}`, {params})
  } 

  createUser(user: any): Observable<any>{
    this.getToken();

    const idAdmin = localStorage.getItem("idAdmin")

    const objetoRol: any = {
      id: Number(idAdmin)
    }
    user.rol = objetoRol

    const params = new HttpParams()
          .set('idAdmin', user.id);

    return this.http.post(`${this.userApiUrl}/crear`, user, {params})
  }

  updateUser(id:number, user: any): Observable<any>{
    this.getToken();

    const idAdmin = localStorage.getItem("idUsuario")
    
    user.id = id

    const objetoRol: any = {
      id: Number(user.rol)
    }
    user.rol = objetoRol

    const params = new HttpParams()
                    .set('idAdmin', Number(idAdmin));

    return this.http.put(`${this.userApiUrl}/actualizar`, user, {params})
  }

  deleteUser(id: number){
    this.getToken();

    const idAdmin = localStorage.getItem("idUsuario")

    const params = new HttpParams()
                    .set('idAdmin', Number(idAdmin));

    return this.http.delete(`${this.userApiUrl}/borrar/${id}`, {params})
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
