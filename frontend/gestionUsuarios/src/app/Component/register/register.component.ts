import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../Services/auth.service';
import { Router } from '@angular/router';
import { UserService } from '../../Services/user.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {

  form !: FormGroup;
  succes!: boolean;
  errors: { [nameError: string]: string } = {};
  rol: any = []
  urlQR!: SafeResourceUrl
  codigoAuthQR!: string

  constructor(private authService: AuthService, private fb: FormBuilder, private router: Router, private userService: UserService, private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      email: ['', Validators.required, this.emailValidator],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    }, {
      validators: this.passwordMatchValidator
    })
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.succes = true
      this.validUser();
    }
  }

  async emailValidator(control: any) {
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (control.value && !emailPattern.test(control.value)) {
      return { invalidEmail: true };
    }
    return null;
  }

  passwordMatchValidator(formGroup: FormGroup): void {
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;

    if (password !== confirmPassword) {
      formGroup.get('confirmPassword')?.setErrors({ noMatch: true });
    } else {
    }
  }

  validUser(): void {
    this.authService.registerUser(this.form.value, (ok: boolean, body: any) => {
      if (!ok) {
        this.errors['email'] = 'REGISTRO FALLIDO, INTENTELO DE NUEVO';
        this.succes = false;
      } else {
        this.succes = true;
        this.urlQR = this.sanitizer.bypassSecurityTrustResourceUrl(`${body.qr}`);
        this.codigoAuthQR = body.secreto;
      }
    })
  }

  createUser(user: any): void {
    this.userService.createUser(user).subscribe((ok: boolean) => {
      if (ok) {
        console.log(`USUARIO CREADO CORRECTAMENTE`);
      }
    });
  }

  get email() {
    return this.form.get('email');
  }

  get password() {
    return this.form.get('password');
  }

  get confirmPassword() {
    return this.form.get('confirmPassword');
  }
}
