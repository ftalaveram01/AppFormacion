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
    return this.http.get(`${this.rolApiUrl}`)
  }
}
