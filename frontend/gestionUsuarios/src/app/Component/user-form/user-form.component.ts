import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../Services/user.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';


@Component({
  selector: 'app-user-form',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css'
})
export class UserFormComponent implements OnInit {

  userForm: FormGroup;
  isUpdate: boolean = false;
  isCreate: boolean = false;
  idUser! : number;
  idAdmin! : number;
  usuarioActualizadoConExito: boolean = false
  usuarioCreadoConExito: boolean = false

  constructor(private userService: UserService, private fb: FormBuilder, private route: ActivatedRoute, private router: Router){
    this.userForm = this.fb.group({
      email: [''],
      password: [''],
      rol: [''],
      router: ['']
    });
  }

  ngOnInit(): void {
    
    this.route.queryParams.subscribe(params =>{
      this.isUpdate = params['isUpdate'] === 'true'
      this.isCreate = params['isCreate'] === 'true'
      if(params['id']){

        this.idUser = +params['id'];
        this.idAdmin = +params['idAdmin']
        this.userService.getUser(this.idUser, this.idAdmin).subscribe(user =>{
          this.userForm.patchValue(user)
        })
      }
    })

  }

  onSubmit(): void{

    if(this.userForm.valid){
      console.log(this.isUpdate)
      console.log(this.isCreate)
      if(this.isUpdate == true){
        this.updateUser(this.idUser,this.userForm.value);
      }else{
        if(this.isCreate == true){
          this.createUser(this.idUser, this.userForm.value);
        } else{
          console.log("ERROR EN EL FORM")
        }
      }
    }

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

}
