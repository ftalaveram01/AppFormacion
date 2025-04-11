import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../Services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RolService } from '../../Services/rol.service';
import { ListboxModule } from 'primeng/listbox';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { AbstractControl } from '@angular/forms';


@Component({
  selector: 'app-user-form',
  imports: [ReactiveFormsModule, CommonModule, ListboxModule, FloatLabelModule, InputTextModule, PasswordModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css'
})
export class UserFormComponent implements OnInit {

  userForm!: FormGroup;
  roles: any[] = [];
  isUpdate: boolean = false;
  isCreate: boolean = false;
  idUser!: number;
  idAdmin!: number;
  usuarioActualizadoConExito: boolean = false
  usuarioCreadoConExito: boolean = false
  errors: { [nameError: string]: string } = {};
  qr: string = "";
  secreto: string = "";

  constructor(private userService: UserService, private rolService: RolService, private fb: FormBuilder, private route: ActivatedRoute, private router: Router) {
    this.userForm = this.fb.group({
      email: ['', Validators.required, this.emailValidator],
      password: ['',[ Validators.required, this.validarPass.bind(this)]],
      confirmPassword: ['', Validators.required],
      rol: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {

    this.rolService.getRoles().subscribe(data => {
      this.roles = data;
    })

    this.route.queryParams.subscribe(params => {
      this.isUpdate = params['isUpdate'] === 'true'
      this.isCreate = params['isCreate'] === 'true'
      if (params['id']) {

        this.idUser = +params['id'];
        this.idAdmin = Number(localStorage.getItem('idUsuario'))
        this.userService.getUser(this.idUser, this.idAdmin).subscribe(user => {
          this.userForm.patchValue(user)
          this.userForm.patchValue({
            rol: user.rol.id
          })
          this.userForm.patchValue({
            password: ''
          })
        })
      }
    })

  }

  onSubmit(): void {
    if (this.isUpdate == true) {
      this.updateUser(this.idUser, this.userForm.value);
      if (this.userForm.value.id == Number(localStorage.getItem('idUsuario'))) {
        alert('El usuario fue correctamente actualizado')
        this.router.navigate(['login'])
      }
    } else {
      if (this.isCreate == true) {
        this.createUser(this.idUser, this.userForm.value);
      }
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

  private updateUser(id: number, user: any): void {
    this.userService.updateUser(id, user).subscribe(response => {
      if (user.id == localStorage.getItem("idUsuario")) {
        this.usuarioActualizadoConExito = true
        this.userForm.reset();
        this.router.navigate(['/login']);
        localStorage.clear();
        return;
      }
      this.usuarioActualizadoConExito = true
      this.userForm.reset();
    },)
  }

  private createUser(id: number, user: any): void {
    this.userService.createUser(user).subscribe({
      next: (data) => {
        this.usuarioCreadoConExito = true
        this.userForm.reset();
        this.qr = data.qr;
        this.secreto = data.secreto;
      },
      error: (error) => {
        alert('Error al crear usuario: ');
      }
    });
  }


  get email() {
    return this.userForm.get('email');
  }

  get password() {
    return this.userForm.get('password');
  }

  get confirmPassword() {
    return this.userForm.get('confirmPassword');
  }

  tieneMayus(): boolean {
    const value = this.userForm.get('password')?.value || '';
    return /[A-Z]/.test(value);
  }
  
  tieneMinus(): boolean {
    const value = this.userForm.get('password')?.value || '';
    return /[a-z]/.test(value);
  }
  
  tieneNumero(): boolean {
    const value = this.userForm.get('password')?.value || '';
    return /[0-9]/.test(value);
  }
  
  tieneCaracterEspecial(): boolean {
    const value = this.userForm.get('password')?.value || '';
    return /[!@#$%^&*(),.?":{}|<>]/.test(value);
  }
  
  tiene8Caracteres(): boolean {
    const value = this.userForm.get('password')?.value || '';
    return value.length >= 8;
  }

}
