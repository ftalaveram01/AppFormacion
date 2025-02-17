import { Component, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user',
  imports: [],
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
  }
  
  btnUpdateUser(isUpdate: boolean, id: number): void{
    this.router.navigate(['user/form'], {
      queryParams: {isUpdate: isUpdate, id: id}
    });
  }
}
