import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../Services/auth.service';
import { LocalStorageService } from '../../Services/localstorage.service';
import { Router } from '@angular/router';
import { UserService } from '../../Services/user.service';

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
  userEmail: string = this.email;
  contadorClick: number = 0;
  mensajeError!: any; 

  constructor(private authService: AuthService,private userService: UserService, private localStorage: LocalStorageService, private router: Router){}

  ngOnInit(): void{}

  onSubmit() {

    if (this.userEmail.localeCompare(this.email) != 0) {
      this.userEmail = this.email;
      this.contadorClick = 0;
    }
  
    if (this.contadorClick >= 5) {
      this.userService.deshabilitar(this.userEmail, (ok: boolean, error?: any) => {
        if (ok) {
          this.mensajeError = error;
        } else {
          this.mensajeError = error;
        }
      });
    } else{

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
          
          this.mensajeError = user
  
          this.contadorClick++
  
          console.log(this.contadorClick)
  
          }
        })
    }  
  }
}
