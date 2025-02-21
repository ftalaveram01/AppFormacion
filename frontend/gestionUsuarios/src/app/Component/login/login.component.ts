import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../Services/auth.service';
import { LocalStorageService } from '../../Services/localstorage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  email: string = '';
  password: string = '';

  constructor(private authService: AuthService, private localStorage: LocalStorageService, private router: Router){}

  ngOnInit(): void{}

  onSubmit(){
    this.authService.loginUser(this.email, this.password, (ok: boolean, user?:any) =>{
      if(ok){
        this.localStorage.setItem('idUsuario',user.id);
        console.log(user.id)
        this.localStorage.setItem('idAdmin',user.rol.id);
        console.log(user.rol.id)
        this.localStorage.setItem('token',user.token);
        console.log(user.token);
        this.router.navigate(['/inicio'], {
          queryParams: {id: user.id, idAdmin: user.rol.id}
        });
        
      }else{
        console.log('FALLO AL HACER LOGIN')
      }
    })
  }


}
