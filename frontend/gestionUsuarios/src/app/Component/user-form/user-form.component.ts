import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../Services/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RolService } from '../../Services/rol.service';


@Component({
  selector: 'app-user-form',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css'
})
export class UserFormComponent implements OnInit {

  userForm!: FormGroup;
  roles: any[] = [];
  isUpdate: boolean = false;
  isCreate: boolean = false;
  idUser! : number;
  idAdmin! : number;
  usuarioActualizadoConExito: boolean = false
  usuarioCreadoConExito: boolean = false
  errors: { [nameError: string]: string} = {};

  constructor(private userService: UserService, private rolService: RolService, private fb: FormBuilder, private route: ActivatedRoute, private router: Router){
    this.userForm = this.fb.group({
      email: ['', Validators.required, this.emailValidator],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      rol: ['']
    }, {
      validators: this.passwordMatchValidator
    })

  }

  ngOnInit(): void {
    
    this.rolService.getRoles().subscribe(data => {
      console.log(data)
      this.roles = data;
    })

    this.route.queryParams.subscribe(params =>{
      this.isUpdate = params['isUpdate'] === 'true'
      this.isCreate = params['isCreate'] === 'true'
      if(params['id']){

        this.idUser = +params['id'];
        this.idAdmin = Number(localStorage.getItem('idUsuario'))
        console.log(this.idAdmin)
        this.userService.getUser(this.idUser, this.idAdmin).subscribe(user =>{
          this.userForm.patchValue(user)
          this.userForm.patchValue({
            rol: user.rol.id
          })
        })
      }
    })

  }

  onSubmit(): void{
      if(this.userForm.valid){
        if(this.isUpdate == true){
          this.updateUser(this.idUser,this.userForm.value);
          if(this.userForm.value.id == Number(localStorage.getItem('idUsuario'))){
            alert('El usuario fue correctamente actualizado')
            this.router.navigate(['login'])
            console.log("Entra")
          }
        }else{
          if(this.isCreate == true){
            this.createUser(this.idUser, this.userForm.value);
            alert('El usuario fue correctamente creado')
            this.router.navigate(['users'])
          } else{
            console.log("ERROR EN EL FORM")
          }
        }
      }


    console.log(this.userForm.value)

  }

  passwordMatchValidator(formGroup: FormGroup) : void{
    const password = formGroup.get('password')?.value;
    const confirmPassword = formGroup.get('confirmPassword')?.value;
  
    if (password !== confirmPassword) {
      formGroup.get('confirmPassword')?.setErrors({ noMatch: true });
    } else {
    }
  }

  async emailValidator(control: any) {
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (control.value && !emailPattern.test(control.value)) {
      return { invalidEmail: true };
    }
    return null;
  }

  private updateUser(id:number, user: any): void{
    this.userService.updateUser(id,user).subscribe(response => {
      if(user.id==localStorage.getItem("idUsuario")){
        this.usuarioActualizadoConExito = true
        this.userForm.reset();
        this.router.navigate(['/login']);
        localStorage.clear();
        return;
      }
      this.usuarioActualizadoConExito = true
      this.userForm.reset();
      console.log('Usuario actualizado');
    },)
  }
  
  private createUser(id:number, user: any): void{
    this.userService.createUser(user).subscribe(response => {
      console.log('Usuario creado correctamente');
      this.usuarioCreadoConExito = true
      this.userForm.reset();
    },)
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

}
