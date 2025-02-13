import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authUrl: string = '';

  constructor(private http: HttpClient) { }

  getUsers(): Observable<any>{

    const token = localStorage.getItem('token');

    const header = {
      headers: new HttpHeaders({
        "Authorization": `Bearer ${token}`
      })
    };

    return this.http.get("http://localhost:8080/usuarios",header);
  }

  loginUser(email: string, password: string, onLogin: (succes: boolean, user?:any) => void){

    const params = new HttpParams()
      .set('email', email)
      .set('password', password);

    this.http.get("http://localhost:8080/autentificacion/login",
     {params}).subscribe((users :any) => {
      onLogin(true,users)
    });

  }

  registerUser(email:string , password: string , onRegister: (succes:boolean)=> void){

    const params = new HttpParams()
      .set('email', email)
      .set('password', password);

    this.http.post("http://localhost:8081/autentificacion/registrar", params).subscribe(() => {
      onRegister(true)
    })

  }

}
