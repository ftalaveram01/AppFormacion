import { Component, OnInit } from '@angular/core';
import { CursoService } from '../../Services/curso.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ConvocatoriaService } from '../../Services/convocatoria.service';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-curso-list',
  imports: [CommonModule],
  templateUrl: './curso-list.component.html',
  styleUrls: ['./curso-list.component.css']
})
export class CursoListComponent implements OnInit {

  cursos: any[] = [];
  idAdmin!: number;
  idUsuario!: number;
  cursoForm: FormGroup;

  constructor(private cursoService: CursoService, private fb: FormBuilder, private router: Router, private convocatoriaServices: ConvocatoriaService) {
    this.cursoForm = this.fb.group({
      nombre: [''],
      descripcion: [''],
      fechaInicio: [''],
      fechaFin: [''],
      router: ['']
    });
  }

  ngOnInit(): void {
    this.cursoService.getCursos().subscribe(data => {
      this.cursos = data.map((curso: any) => {
        return {
          ...curso,
        };
      });
      if (this.isAdmin()) {

      } else {
        this.cursos = this.cursos.filter((curso) => {
          return curso.habilitado === true
        })
      }

    });

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const tokenDecoded: any = jwtDecode(token)

    this.idAdmin = tokenDecoded.rol[0].rol.id;
    this.idUsuario = tokenDecoded.sub;
  }

  deleteCurso(id: number, nombreCurso: string): void {
    const ok = confirm("¿Estas seguro que deseas eliminar el curso " + nombreCurso.toUpperCase() + " ?")
    if (ok) {
      this.cursoService.deleteCurso(id).subscribe(() => {
        this.ngOnInit();
      })
    }
  }

  btnUpdateCurso(isUpdate: boolean, id: number): void {
    this.router.navigate(['cursos/form'], {
      queryParams: { isUpdate: isUpdate, id: id }
    });
  }

  btnMatricularse(idCurso: number): void {
    this.cursoService.addUsuarioToCurso(idCurso, this.idUsuario).subscribe(() => {
      this.cursoService.getCursos().subscribe(data => {
        this.cursos = data.map((curso: any) => {
          const fechaInicio = new Date(curso.fechaInicio);
          const fechaFin = new Date(curso.fechaFin);
          return {
            ...curso,
            fechaInicio: this.formatearFecha(fechaInicio),
            fechaFin: this.formatearFecha(fechaFin)
          };
        });
        this.cursos = this.cursos.filter(curso => curso.habilitado === true)
      });
    })
  }

  btnDesmatricularse(idCurso: number): void {
    this.cursoService.deleteUsuarioFromCurso(this.idUsuario, idCurso).subscribe(() => {
      this.cursoService.getCursos().subscribe(data => {
        this.cursos = data.map((curso: any) => {
          const fechaInicio = new Date(curso.fechaInicio);
          const fechaFin = new Date(curso.fechaFin);
          return {
            ...curso,
            fechaInicio: this.formatearFecha(fechaInicio),
            fechaFin: this.formatearFecha(fechaFin)
          };
        });
        this.cursos = this.cursos.filter(curso => curso.habilitado === true)
      });
    })
  }


  btnDeleteUserCourse(id: number, idCurso: number): void {
    this.cursoService.deleteUsuarioFromCurso(id, idCurso).subscribe(() => {
      this.cursos = this.cursos.filter(u => u.id !== id)
      this.cursoService.getCursos().subscribe(data => {
        this.cursos = data.map((curso: any) => {
          const fechaInicio = new Date(curso.fechaInicio);
          const fechaFin = new Date(curso.fechaFin);
          return {
            ...curso,
            fechaInicio: this.formatearFecha(fechaInicio),
            fechaFin: this.formatearFecha(fechaFin)
          };
        });
      });
    })
  }


  isInscrito(idCurso: number): boolean {
    const curso = this.cursos.find(c => c.id === idCurso);
    if (!curso)
      return false;

    return curso.usuarios.some((u: any) => u.id == this.idUsuario);
  }

  borrarCache(): void {
    localStorage.clear();
  }

  btnCreateConvocatoria(isUpdate: boolean, isCreate: boolean, curso: any): void {

    this.router.navigate(['convocatorias/form'], {
      queryParams: { idCurso: curso.id, isUpdate: isUpdate, isCreate: isCreate }
    })

  }

  private formatearFecha(fecha: Date): string {
    const anio = fecha.getFullYear();
    const mes = (fecha.getMonth() + 1).toString().padStart(2, '0');
    const dia = fecha.getDate().toString().padStart(2, '0');
    const hora = fecha.getHours().toString().padStart(2, '0');
    const minutos = fecha.getMinutes().toString().padStart(2, '0');

    const fechaFormateada = `${dia}/${mes}/${anio} ${hora}:${minutos}`;

    return fechaFormateada
  }

  isAdmin(): boolean {
    return this.idAdmin == 0;
  }
}
