import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { ConvocatoriaService } from '../../Services/convocatoria.service';
import { UserService } from '../../Services/user.service';

@Component({
  selector: 'app-convocatoria',
  imports: [CommonModule, FormsModule],
  templateUrl: './convocatoria.component.html',
  styleUrl: './convocatoria.component.css'
})
export class ConvocatoriaComponent {
  convocatorias: any[] = [];
  convocatoriasFiltradas: any[] = [];
  idAdmin!: number;
  filtroEstado: string = 'activas'; // Estado por defecto del filtro

  constructor(
    private convocatoriaService: ConvocatoriaService,
    private router: Router,
    private usuariosSerivices: UserService
  ) { }

  ngOnInit(): void {
    this.idAdmin = Number(localStorage.getItem('idAdmin'));

    if (this.isAdmin()) {
      this.convocatoriaService.getConvocatorias().subscribe((data) => {
        this.convocatorias = data;
        this.filtrarConvocatorias(); // Aplicar el filtro al inicializar
      });
    } else {
      this.convocatoriaService.getConvocatoriasActivas().subscribe((data) => {
        this.convocatorias = data;
        this.convocatoriasFiltradas = data; // Los usuarios normales solo ven convocatorias activas
      });
    }
  }

  filtrarConvocatorias(): void {
    if (this.filtroEstado === 'activas') {
      this.convocatoriasFiltradas = this.convocatorias.filter(
        (c) =>
          c.estado === 'EN_PREPARACION' ||
          c.estado === 'CONVOCADA' ||
          c.estado === 'EN_CURSO'
      );
    } else if (this.filtroEstado === 'inactivas') {
      this.convocatoriasFiltradas = this.convocatorias.filter(
        (c) => c.estado === 'TERMINADA' || c.estado === 'DESIERTA'
      );
    } else {
      this.convocatoriasFiltradas = [...this.convocatorias];
    }
  }

  btnUpdateConvocatoria(isUpdate: boolean, isCreate: boolean, id: number): void {
    this.router.navigate(['convocatorias/form'], {
      queryParams: { idConvocatoria: id, isUpdate: isUpdate, isCreate: isCreate }
    });
  }

  btnDeleteConvocatoria(id: number): void {
    this.convocatoriaService.deleteConvocatoria(id).subscribe(() => {
      this.convocatorias = this.convocatorias.filter((c) => c.id !== id);
      this.filtrarConvocatorias(); // Volver a aplicar el filtro tras eliminar
    });
  }

  borrarCache(): void {
    localStorage.clear();
  }

  isAdmin(): boolean {
    return this.idAdmin === 0;
  }

  isUsuarioOnCurso(idConvocatoria: number): boolean {
    const convocatoria = this.convocatorias.find((c) => c.id === idConvocatoria);
    return convocatoria?.usuarios.some(
      (u: { id: number }) => u.id === Number(localStorage.getItem('idUsuario'))
    );
  }
}
