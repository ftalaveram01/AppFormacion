import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../Services/auth.service';
import { Router } from '@angular/router';
import { UserService } from '../../Services/user.service';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { KeyFilterModule } from 'primeng/keyfilter';
import { AbstractControl } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, InputTextModule, PasswordModule, KeyFilterModule],
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
  bloquearEspacios: RegExp = /^[^\s]*$/;

  constructor(private authService: AuthService, private fb: FormBuilder, private router: Router, private userService: UserService, private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required, this.validarPass.bind(this)]],
      confirmPassword: ["", [Validators.required]]
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.succes = true
      this.validUser();
    }
  }

  validarPass(control: AbstractControl): { [key: string]: boolean } | null {
    const value = control.value;
    if (!value) return null;
  
    const hasUpperCase = /[A-Z]/.test(value);
    const hasLowerCase = /[a-z]/.test(value);
    const hasNumber = /[0-9]/.test(value);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(value);
    const hasEightChars = value.length >= 8;
    const isValid = hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar && hasEightChars;
  
    return !isValid ? { passwordComplexity: true } : null;
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

  tieneMayus(): boolean {
    const value = this.form.get('password')?.value || '';
    return /[A-Z]/.test(value);
  }
  
  tieneMinus(): boolean {
    const value = this.form.get('password')?.value || '';
    return /[a-z]/.test(value);
  }
  
  tieneNumero(): boolean {
    const value = this.form.get('password')?.value || '';
    return /[0-9]/.test(value);
  }
  
  tieneCaracterEspecial(): boolean {
    const value = this.form.get('password')?.value || '';
    return /[!@#$%^&*(),.?":{}|<>]/.test(value);
  }
  
  tiene8Caracteres(): boolean {
    const value = this.form.get('password')?.value || '';
    return value.length >= 8;
  }
}
