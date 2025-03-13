import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ConvocatoriaService } from '../../Services/convocatoria.service';
import { UserService } from '../../Services/user.service';


@Component({
  selector: 'app-convocatoria',
  imports: [CommonModule],
  templateUrl: './convocatoria.component.html',
  styleUrl: './convocatoria.component.css'
})
export class ConvocatoriaComponent {

  convocatorias: any[] = [];
  idAdmin!: number;
  formulario!: FormGroup

  constructor(private convocatoriaService: ConvocatoriaService, private router: Router, private usuariosSerivices: UserService) {
  }

  ngOnInit(): void {
    this.convocatoriaService.getConvocatorias().subscribe((data) => {
      this.convocatorias = data;
    })
    this.formulario = new FormGroup({
      idCurso: new FormControl('')
    })
    this.idAdmin = Number(localStorage.getItem('idAdmin'))
    if(!this.isAdmin){
      this.convocatorias.filter((convocatoria) => {
        return convocatoria.estado === 'CONVOCADA'
      })
    }
  }

  btnCreateConvocatoria(isUpdate: boolean, isCreate: boolean): void {
   
  }

  btnUpdateConvocatoria(isUpdate: boolean, isCreate: boolean, id: number): void {
    console.log(isCreate)
    console.log(isUpdate)
    this.router.navigate(['Convocatoria/form'], {
      queryParams: { isUpdate: isUpdate, isCreate: isCreate, id: id }
    });
  }

  btnDeleteConvocatoria(id: number): void {
    this.convocatoriaService.deleteConvocatoria(id).subscribe(() => {
      this.convocatorias = this.convocatorias.filter(c => c.id !== id);
    });
  }

  btnInscribirseConvocatoria(idConvocatoria: number):void {
    this.convocatoriaService.inscribirEnConvocatoria(idConvocatoria).subscribe(response =>{
      this.convocatorias = response;
    })
  }

  puedeInscribirse(estado: string, size: number):boolean{
    if(estado == "EN PREPARACION" && size<15){
      return true;
    }
    return false;
  }


  borrarCache(): void {
    localStorage.clear();
  }

  isAdmin(): boolean {
    return this.idAdmin == 0;
  }

}
