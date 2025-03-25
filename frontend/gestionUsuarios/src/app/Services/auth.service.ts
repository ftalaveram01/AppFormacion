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

    return this.http.get("http://localhost:8080/usuarios", header);
  }

  loginUser(email: string, password: string, verifyCode: string, onLogin: (succes: boolean, user?: any) => void) {

    const params = new HttpParams()
      .set('email', email)
      .set('password', password)
      .set('otpCode', verifyCode);


    this.http.get("http://localhost:8100/autentificacion/login",
      { params }).subscribe((jwt: any) => {
        if (jwt) {
          onLogin(true, jwt)
        }
      },
        (error) => {
          if (error.status === 400) {
            onLogin(false, error.error)
          }
        });

  }

  registerUser(user: any, onRegister: (succes: boolean, body: any) => void) {
    const numRol: number = 1;

    const objetoRol: any = {
      id: numRol
    }
    user.rol = objetoRol
    user.habilitado = 1

    this.http.post("http://localhost:8101/autentificacion/registrar", user).subscribe(
      (data) => {
        onRegister(true, data)
      },
      (error) => {
        onRegister(false, error)
      }
    )
  }


}
