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
  verifyCode: string = '';
  userEmail: string = this.email;
  contadorClick: number = 0;
  mensajeError!: any;

  constructor(private authService: AuthService, private userService: UserService, private localStorage: LocalStorageService, private router: Router) { }

  ngOnInit(): void { }

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
    } else {

      this.authService.loginUser(this.email, this.password, this.verifyCode, (ok: boolean, token?: any) => {
        if (ok) {
          localStorage.setItem("token", JSON.stringify(token));
          this.router.navigate(['/inicio']);
        } else {

          this.mensajeError = token

          this.contadorClick++

          console.log(this.contadorClick)

        }
      })
    }
  }
}
