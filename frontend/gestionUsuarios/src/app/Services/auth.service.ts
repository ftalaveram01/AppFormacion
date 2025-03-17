import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authUrl: string = '';

  constructor(private http: HttpClient) { }

  getUsers(): Observable<any> {

    const token = localStorage.getItem('token');

    const header = {
      headers: new HttpHeaders({
        "Authorization": `Bearer ${token}`
      })
    };

    return this.http.get("http://localhost:8086/usuarios", header);
  }

  loginUser(email: string, password: string, onLogin: (succes: boolean, user?: any) => void) {

    const params = new HttpParams()
      .set('email', email)
      .set('password', password);


    this.http.get("http://localhost:8086/autentificacion/login",
      { params }).subscribe((users: any) => {
        if (users) {
          onLogin(true, users)
        }
      },
        (error) => {
          if (error.status === 400) {
            onLogin(false, error.error)
          }
        });

  }

  registerUser(user: any, onRegister: (succes: boolean) => void) {
    const numRol: number = 1;

    const objetoRol: any = {
      id: numRol
    }
    user.rol = objetoRol
    user.habilitado = 1

    this.http.post("http://localhost:8081/autentificacion/registrar", user).subscribe(
      () => {
        onRegister(true)
      },
      (error) => {
        onRegister(false)
      }
    )
  }


}
