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
     this.ngOnInit()
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

  btnGenerarReporte(): void {
    this.userService.obtenerReporte().subscribe((blob: Blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'ReporteUsuario.pdf';
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
  }, error => {
      console.error('Error descargando el PDF', error);
  });
  }

  borrarCache(): void {
    localStorage.clear();
  }
}
