import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RolService } from '../../Services/rol.service';

@Component({
  selector: 'app-rol',
  imports: [CommonModule],
  templateUrl: './rol.component.html',
  styleUrl: './rol.component.css'
})
export class RolComponent {

  roles: any[] = [];

  constructor(private rolService: RolService, private router: Router) {

  }

  btnCreateRol(isUpdate: boolean, isCreate: boolean): void{
    console.log(isCreate)
    console.log(isUpdate)
    this.router.navigate(['rols/form'], {
      queryParams: {isUpdate: isUpdate, isCreate: isCreate}
    })
  }

  ngOnInit(): void{
    this.rolService.getRoles().subscribe((data) => {
      this.roles = data;
    })
  }

  borrarCache(): void {
    localStorage.clear();
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
