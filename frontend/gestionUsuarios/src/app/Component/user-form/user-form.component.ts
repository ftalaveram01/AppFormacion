import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../Services/user.service';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-form',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './user-form.component.html',
  styleUrl: './user-form.component.css'
})
export class UserFormComponent implements OnInit {

  userForm: FormGroup;
  isUpdate: boolean = false;
  idUser! : number;

  constructor(private userService: UserService, private fb: FormBuilder, private route: ActivatedRoute){
    this.userForm = this.fb.group({
      email: [''],
      password: [''],
      rol: ['']
    });
  }

  ngOnInit(): void {
    
    this.route.queryParams.subscribe(params =>{
      this.isUpdate = params['isUpdate']
      if(params['id']){
        this.idUser = +params['id'];
        this.userService.getUser(this.idUser).subscribe(user =>{
          this.userForm.patchValue(user)
        })
      }
    })

  }

  onSubmit(): void{

    if(this.userForm.valid){
      if(this.isUpdate){
        this.updateUser(this.idUser,this.userForm.value);
      }else{
        this.createUser(this.userForm.value);
      }
    }

  }

  private updateUser(id:number, user: any): void{
    this.userService.updateUser(id,user).subscribe(response => {
      console.log('Usuario actualizado');
      this.userForm.reset();
    },)
  }

  private createUser(user: any): void{
    this.userService.createUser(user).subscribe(response => {
      console.log('Usuario creado correctamente');
      this.userForm.reset();
    },)
  }

}
