import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ConvocatoriaService } from '../../Services/convocatoria.service';


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

  constructor(private convocatoriaService: ConvocatoriaService, private router: Router) {
  }

  ngOnInit(): void {
    this.convocatoriaService.getConvocatorias().subscribe((data) => {
      this.convocatorias = data;
    })
    this.formulario = new FormGroup({
      idCurso: new FormControl('')
    })
    this.idAdmin = Number(localStorage.getItem('idAdmin'))
    if(!this.isAdmin()){
      this.convocatorias.filter((convocatoria) => {
        return convocatoria.estado === 'CONVOCADA'
      })
    }
  }

  btnCreateConvocatoria(isUpdate: boolean, isCreate: boolean): void {
    console.log(isCreate)
    console.log(isUpdate)
    this.router.navigate(['convocatoria/form'], {
      queryParams: { isUpdate: isUpdate, isCreate: isCreate }
    })
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

  borrarCache(): void {
    localStorage.clear();
  }

  isAdmin(): boolean {
    return this.idAdmin == 0;
  }

  isUsuarioOnCurso(){
    return this.convocatorias.find(convocatoria => convocatoria.usuarios.id === Number(localStorage.getItem('idUsuario')))
  }

}
