import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RolService } from '../../Services/rol.service';


@Component({
  selector: 'app-rol',
  imports: [],
  templateUrl: './rol.component.html',
  styleUrl: './rol.component.css'
})
export class RolComponent {

  roles: any[] = [];

  constructor(private rolService: RolService, private router: Router) {

  }

  btnUpdateRol(isUpdate: boolean, id: number): void{
    this.router.navigate(['rol/form'], {
      queryParams: {isUpdate: isUpdate, id: id}
    });
  }

  deleteRol(id: number): void{
    this.rolService.deleteRol(id).subscribe(() =>{
      this.roles = this.roles.filter(r => r.id !== id);
    })
  }
  
}
