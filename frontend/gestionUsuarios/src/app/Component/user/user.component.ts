import { Component, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user',
  imports: [CommonModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css'
})
export class UserComponent implements OnInit {

  users: any[] = [];

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit(): void {
   this.userService.getAllUsers().subscribe(data => {
    this.users = data;
   });
  }

  deleteUser(id: number): void{
    this.userService.deleteUser(id).subscribe(() => {
      this.users = this.users.filter( u => u.id !== id);
    });

    if(id===Number(localStorage.getItem("idUsuario"))){
      this.router.navigate(['/login']);
      localStorage.clear();
    }
  }
  
  btnUpdateUser(isUpdate: boolean, isCreate: boolean, id: number): void{
    this.router.navigate(['user/form'], {
      queryParams: {isUpdate: isUpdate, isCreate: isCreate, id: id}
    });
  }

  btnCreateUser(isUpdate: boolean, isCreate: boolean): void{
    console.log(isCreate)
    console.log(isUpdate)
    this.router.navigate(['user/form'], {
      queryParams: {isUpdate: isUpdate, isCreate: isCreate}
    })
  }

  borrarCache(): void {
    localStorage.clear();
  }
}
