import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RolService } from '../../Services/rol.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-rol',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './rol.component.html',
  styleUrl: './rol.component.css'
})
export class RolComponent {

  roles: any[] = [];
  formulario!: FormGroup
  resultado: any[] = []
  filtrado: boolean = false

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
    this.formulario = new FormGroup({
      nombreRol: new FormControl('')
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

  buscarRol(): void{

    console.log(this.roles)

    this.resultado = this.roles.filter(rol => {
      return rol.nombreRol.toLowerCase().includes(this.formulario.value.nombreRol)
    })
    this.filtrado = true

    console.log(this.resultado)
  }
  
}
