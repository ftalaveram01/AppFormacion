import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../Services/auth.service';
import { Router } from '@angular/router';
import { UserService } from '../../Services/user.service';

@Component({
  selector: 'app-register',
  standalone : true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {

  form : FormGroup;
  succes!: boolean;
  errors: { [nameError: string]: string} = {};
  rol: any = []

  constructor(private authService: AuthService, private fb: FormBuilder, private router: Router, private userService: UserService){

    this.form = this.fb.group({
      email: ['',Validators.required, Validators.email],
      password: ['', Validators.required, Validators.minLength(20)],
      confirmPassword: ['',Validators.required, Validators.minLength(20)]
    })

  }

  ngOnInit(): void{}

  onSubmit() :void{
    this.validForm();
    this.validPass();
    this.validUser();
    //this.createUser(this.form.value);
  }

  validForm() :void{
    if(!this.form.valid){
      console.log("ERROR NO SE HA PODIDO REGISTRAR AL USUARIO")
      this.errors['not valid'] = 'ERROR NO SE HA PODIDO REGISTRAR AL USUARIO';
      return;
    }
  }

  validPass() : void{
    if(this.form.get('password')?.value != this.form.get('confirmPassword')?.value){
      this.errors['confirmPassword'] = 'LAS CONTRASEÑAS NO COINCIDEN';
      return;
    }
  }

  validUser(): void{
    this.authService.registerUser(this.form.value, (ok : boolean) => {
      if(!ok){
        this.errors['email'] = 'REGISTRO FALLIDO, INTENTELO DE NUEVO';
      }else{
        this.succes = true;
      }
    })
  }

  createUser(user: any): void{
    this.userService.createUser(user).subscribe((ok:boolean) =>{
      if(ok){
        console.log(`USUARIO CREADO CORRECTAMENTE`);
      }
    });
  }
}
