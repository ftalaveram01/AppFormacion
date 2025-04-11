import { Component, OnInit } from '@angular/core';
import { CursoService } from '../../Services/curso.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, FormsModule } from '@angular/forms';
import { ConvocatoriaService } from '../../Services/convocatoria.service';
import { jwtDecode } from 'jwt-decode';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ButtonModule } from 'primeng/button';

@Component({
  selector: 'app-curso-list',
  imports: [CommonModule, ToastModule, ConfirmDialogModule, ButtonModule, FormsModule],
  templateUrl: './curso-list.component.html',
  styleUrls: ['./curso-list.component.css'],
  providers: [MessageService, ConfirmationService]
})
export class CursoListComponent implements OnInit {

  cursos: any[] = [];
  cursosFiltrados: any[] = [];
  idAdmin!: number;
  idUsuario!: number;
  cursoForm: FormGroup;
  filtroEstado: string = 'habilitados';
  buscarNombre: string = '';
  filtroHoras: number = 0;
  

  constructor(private cursoService: CursoService, 
    private fb: FormBuilder, 
    private router: Router, 
    private convocatoriaServices: ConvocatoriaService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService) {
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
      
      this.filtrarCursos();

    });

    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const tokenDecoded: any = jwtDecode(token)

    this.idAdmin = tokenDecoded.rol[0].rol.id;
    this.idUsuario = tokenDecoded.sub;
  }

  filtrarCursos(): void {
    if (this.filtroEstado === 'habilitados') {
      this.cursosFiltrados = this.cursos.filter(
        (c) => c.habilitado == true
      );
    } else if (this.filtroEstado === 'deshabilitados') {
      this.cursosFiltrados = this.cursos.filter(
        (c) => c.habilitado == false
      );
    } else {
      this.cursosFiltrados = [...this.cursos];
    }
    
    if (this.buscarNombre) {
      this.cursosFiltrados = this.cursosFiltrados.filter(
        (c) => c.nombre.toLowerCase().includes(this.buscarNombre.toLowerCase())
      );
    }
  
    if (this.filtroHoras !== null) {
      this.cursosFiltrados = this.cursosFiltrados.filter(
        (c) => c.numeroHoras >= this.filtroHoras
      );
    }
  }

  habilitarCurso(curso:any): void {

    this.confirmationService.confirm({
      message: 'Será visible para todos los usuarios y podrán matricularse. Podrá deshabilitarlo cuando desee.',
      header: '¿Estás seguro?',
      closable: true,
      closeOnEscape: true,
      icon: 'pi pi-exclamation-triangle',
      rejectButtonProps: {
          label: 'Cancelar',
          severity: 'secondary',
          outlined: true,
      },
      acceptButtonProps: {
          label: 'Habilitar',
          severity: 'primary',
      },
      accept: () => {
        this.cursoService.updateCurso(curso.id, curso, curso.usuarios).subscribe({
          next: () => {
            this.ngOnInit();
            this.messageService.add({severity:'success', 
              summary:'Correcto', 
              detail:`El curso "${curso.nombre}" ha sido habilitado.`,
              life: 5000 });
          },
          error: () => {
            this.messageService.add({severity:'error', 
              summary:'Error', 
              detail:`No se ha podido habilitar el curso, inténtalo más tarde.`,
              life: 5000 });
          }
        })
      },
      reject: () => {
        this.messageService.add({severity:'info', 
          summary:'Vuelta atrás', 
          detail:`Has decidido no habilitar el curso.`,
          life: 5000 });
      },
    });
  }

  deleteCurso(id: number, nombreCurso: string): void {

    this.confirmationService.confirm({
      message: 'Finalizaran todas sus convocatorias en curso y sus usuarios serán desmatriculados. Puede volverlo a habilitar pero esto será irreversible.',
      header: '¿Estás seguro?',
      closable: true,
      closeOnEscape: true,
      icon: 'pi pi-exclamation-triangle',
      rejectButtonProps: {
          label: 'Cancelar',
          severity: 'secondary',
          outlined: true,
      },
      acceptButtonProps: {
          label: 'Borrar',
          severity: 'danger',
      },
      accept: () => {
        this.cursoService.deleteCurso(id).subscribe({
          next: () => {
            this.ngOnInit();
            this.messageService.add({severity:'success', 
              summary:'Correcto', 
              detail:`El curso "${nombreCurso}" ha sido eliminado.`,
              life: 5000 });
          },
          error: () => {
            this.messageService.add({severity:'error', 
              summary:'Error', 
              detail:`No se ha podido eliminar el curso, inténtalo más tarde.`,
              life: 5000 });
          }
        })
      },
      reject: () => {
        this.messageService.add({severity:'info', 
          summary:'Vuelta atrás', 
          detail:`Has decidido no eliminar el curso.`,
          life: 5000 });
      },
    });
  }

  btnUpdateCurso(isUpdate: boolean, id: number): void {
    this.router.navigate(['cursos/form'], {
      queryParams: { isUpdate: isUpdate, id: id }
    });
  }

  btnMatricularse(idCurso: number, nombre: string): void {
    this.cursoService.addUsuarioToCurso(idCurso, this.idUsuario).subscribe({
      next: () => {
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

        this.messageService.add({severity:'success', 
          summary:'Correcto', 
          detail:`Te has matriculado en el curso ${nombre}.`,
          life: 5000 });
      },
      error: () => {
        this.messageService.add({severity:'error', 
          summary:'Error', 
          detail:`No te has podido matricular en el curso, inténtalo más tarde.`,
          life: 5000 });
      }
    })
  }

  btnDesmatricularse(idCurso: number, nombre :string): void {
    this.cursoService.deleteUsuarioFromCurso(this.idUsuario, idCurso).subscribe({
      next: () => {
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

        this.messageService.add({severity:'success', 
          summary:'Correcto', 
          detail:`Te has desmatriculado del curso ${nombre}.`,
          life: 5000 });

      },
      error: () => {
        this.messageService.add({severity:'error', 
          summary:'Error', 
          detail:`No te has podido desmatricular del curso, inténtalo más tarde.`,
          life: 5000 });
      }
    })
  }

  btnDeleteUserCourse(id: number, idCurso: number): void {

    this.confirmationService.confirm({
      message: 'Si está en una convocatoria en curso será eliminado de ella. Esto es completamente irreversible.',
      header: '¿Estás seguro?',
      closable: true,
      closeOnEscape: true,
      icon: 'pi pi-exclamation-triangle',
      rejectButtonProps: {
          label: 'Cancelar',
          severity: 'secondary',
          outlined: true,
      },
      acceptButtonProps: {
          label: 'Borrar',
          severity: 'danger',
      },
      accept: () => {
        this.cursoService.deleteUsuarioFromCurso(id, idCurso).subscribe({
          next: () => {
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
    
            this.messageService.add({severity:'success', 
              summary:'Correcto', 
              detail:`El usuario  "${id}" ha sido eliminado del curso "${idCurso}".`,
              life: 5000 });
    
          },
          error: () => {
            this.messageService.add({severity:'error', 
              summary:'Error', 
              detail:`No se ha podido eliminar al usuario del curso.`,
              life: 5000 });
          }
        });
      },
      reject: () => {
        this.messageService.add({severity:'info', 
          summary:'Vuelta atrás', 
          detail:`Has decidido no quitar al usuario del curso.`,
          life: 5000 });
      },
    });


    
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
      queryParams: { idCurso: curso.id, isUpdate: isUpdate, isCreate: isCreate, isCurso: true }
    })

  }

  btnGenerarReporte(): void {
    this.messageService.add({severity:'info', 
      summary:'En Proceso', 
      detail:`Generando el reporte, puede tardar unos segundos...`,
      life: 5000 });
    this.cursoService.obtenerReporte().subscribe((blob: Blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'ReporteCurso.pdf';
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      this.messageService.add({severity:'success', 
        summary:'Correcto', 
        detail:`Reporte generado.`,
        life: 5000 });
  }, error => {
    this.messageService.add({severity:'error', 
      summary:'Error', 
      detail:`No se ha podido generar el reporte, disculpe las molestias.`,
      life: 5000 });
  });
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
