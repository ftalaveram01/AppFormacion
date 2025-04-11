import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ConvocatoriaService } from '../../Services/convocatoria.service';
import { UserService } from '../../Services/user.service';
import { jwtDecode } from 'jwt-decode';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@Component({
  selector: 'app-convocatoria',
  imports: [CommonModule, FormsModule, ToastModule, ConfirmDialogModule],
  templateUrl: './convocatoria.component.html',
  styleUrl: './convocatoria.component.css',
  providers: [MessageService, ConfirmationService]
})
export class ConvocatoriaComponent {
  convocatorias: any[] = [];
  convocatoriasFiltradas: any[] = [];
  idAdmin!: number;
  filtroEstado: string = 'activas';

  constructor(
    private convocatoriaService: ConvocatoriaService,
    private router: Router,
    private usuariosSerivices: UserService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) { }

  ngOnInit(): void {
    const storedToken = localStorage.getItem('token');
    const token = storedToken ? JSON.parse(storedToken).token : null;

    const tokenDecoded: any = jwtDecode(token)

    this.idAdmin = tokenDecoded.rol[0].rol.id;

    if (this.isAdmin()) {
      this.convocatoriaService.getConvocatorias().subscribe((data) => {
        this.convocatorias = data;
        this.filtrarConvocatorias();
      });
    } else {
      this.convocatoriaService.getConvocatoriasActivas().subscribe((data) => {
        this.convocatorias = data;
        this.convocatoriasFiltradas = data;
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

    this.confirmationService.confirm({
      message: 'Borrar una convocatoria es un acto irreversible, pasará a DESIERTA.',
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
        this.convocatoriaService.deleteConvocatoria(id).subscribe({
          next: () => {
            this.convocatorias = this.convocatorias.filter((c) => c.id !== id);
            this.convocatoriaService.getConvocatorias().subscribe((data) => {
              this.convocatorias = data;
            });
            this.filtrarConvocatorias();
    
            this.messageService.add({severity:'success', 
              summary:'Correcto', 
              detail:`La convocatoria se ha borrado correctamente.`,
              life: 5000 });
          },
          error: () => {
            this.messageService.add({severity:'error', 
              summary:'Error', 
              detail:`No se ha podido eliminar la convocatoria.`,
              life: 5000 });
          }
        });
      },
      reject: () => {
        this.messageService.add({severity:'info', 
          summary:'Vuelta atrás', 
          detail:`Has decidido no borrar la convocatoria.`,
          life: 5000 });
      },
    });


  }

  btnGenerarReporte(): void {
    this.convocatoriaService.obtenerReporte().subscribe((blob: Blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'ReporteConvocatoria.pdf';
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
