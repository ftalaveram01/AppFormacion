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
    return this.http.get(`${this.userApiUrl}`)
  }

  getUser(id:number, idAdmin:number): Observable<any>{
    this.getToken();
    const params = new HttpParams().set('idAdmin', idAdmin)
    return this.http.get(`${this.userApiUrl}/${id}`, {params})
  } 

  createUser(user: any): Observable<any>{
    this.getToken();
    const params = new HttpParams()
          .set('email', user.email)
          .set('password', user.password)
          .set('rol', user.rol);
    return this.http.post(`${this.userApiUrl}/crear`, params)
  }

  updateUser(id:number, user: any): Observable<any>{
    this.getToken();
    return this.http.put(`${this.userApiUrl}/actualizar`, user)
  }

  deleteUser(id: number){
    this.getToken();
    return this.http.delete(`${this.userApiUrl}/borrar/${id}`)
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
