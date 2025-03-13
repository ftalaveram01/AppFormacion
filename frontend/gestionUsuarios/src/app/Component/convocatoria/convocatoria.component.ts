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

  constructor(private convocatoriaService: ConvocatoriaService, private router: Router) {
  }

  ngOnInit(): void {
    this.idAdmin = Number(localStorage.getItem('idAdmin'))

    if (this.isAdmin()) {
      this.convocatoriaService.getConvocatorias().subscribe((data) =>
        this.convocatorias = data
      )

    } else {
      this.convocatoriaService.getConvocatoriasActivas().subscribe(data =>
        this.convocatorias = data
      )
    }
  }

  btnUpdateConvocatoria(isUpdate: boolean, isCreate: boolean, id: number): void {
    console.log(isCreate)
    console.log(isUpdate)
    this.router.navigate(['convocatorias/form'], {
      queryParams: { idConvocatoria: id, isUpdate: isUpdate, isCreate: isCreate }
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

  isUsuarioOnCurso(idConvocatoria: Number): boolean {
    const convocatoria = this.convocatorias.find(convocatoria => convocatoria.id === idConvocatoria)
    return convocatoria.usuarios.find((usuario: { id: number; }) => usuario.id === Number(localStorage.getItem('idUsuario')))
  }

  isUsuariosConvocatoriaVacio(idConvocatoria: Number): boolean {
    return this.convocatorias.find(convocatoria => convocatoria.id === idConvocatoria)?.usuarios.length == 0
  }

}
