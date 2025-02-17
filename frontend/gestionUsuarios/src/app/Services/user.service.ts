import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private userApiUrl = '';

  constructor(private http: HttpClient) { }

  getAllUsers(): Observable<any>{
    this.getToken();
    return this.http.get(`${this.userApiUrl}`)
  }

  getUser(id:number): Observable<any>{
    this.getToken();
    return this.http.get(`${this.userApiUrl}/${id}`)
  } 

  createUser(user: any): Observable<any>{
    this.getToken();
    return this.http.post(`${this.userApiUrl}`,user)
  }

  updateUser(id:number, user: any): Observable<any>{
    this.getToken();
    return this.http.put(`${this.userApiUrl}/${id}`, user)
  }

  deleteUser(id: number){
    this.getToken();
    return this.http.delete(`${this.userApiUrl}/${id}`)
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
