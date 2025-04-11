import { Component, OnInit } from '@angular/core';
import { UserService } from '../../Services/user.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@Component({
  selector: 'app-user',
  imports: [CommonModule, ToastModule, ConfirmDialogModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css',
  providers: [MessageService, ConfirmationService]
})
export class UserComponent implements OnInit {

  users: any[] = [];

  constructor(
    private userService: UserService, 
    private router: Router,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
   this.userService.getAllUsers().subscribe(data => {
    this.users = data;
   });
  }

  deleteUser(id: number): void{

    this.confirmationService.confirm({
      message: 'Borrar un usuario lo dejará inactivo. Puede volver a habilitarlo si lo desea.',
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
        this.userService.deleteUser(id).subscribe({
          next: () => {
            if(id===Number(localStorage.getItem("idUsuario"))){
              this.router.navigate(['/login']);
              localStorage.clear();
            }
    
            this.ngOnInit()
            this.messageService.add({severity:'success', 
              summary:'Correcto', 
              detail:`El usuario se ha borrado correctamente.`,
              life: 5000 });
          },
          error: () => {
            this.messageService.add({severity:'error', 
              summary:'Error', 
              detail:`No se ha podido eliminar el usuario.`,
              life: 5000 });
          }
    
        });
      },
      reject: () => {
        this.messageService.add({severity:'info', 
          summary:'Vuelta atrás', 
          detail:`Has decidido no borrar el usuario.`,
          life: 5000 });
      },
    });
  }

  habilitarUser(id: number): void{

    this.confirmationService.confirm({
      message: 'Al habilitar el usuario será accesible. Puede volver a deshabilitarlo si lo desea.',
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
        this.userService.habilitarUser(id).subscribe({
          next: () => {
    
            this.ngOnInit()
            this.messageService.add({severity:'success', 
              summary:'Correcto', 
              detail:`El usuario se ha habilitado correctamente.`,
              life: 5000 });
          },
          error: () => {
            this.messageService.add({severity:'error', 
              summary:'Error', 
              detail:`No se ha podido habilitar el usuario.`,
              life: 5000 });
          }
    
        });
      },
      reject: () => {
        this.messageService.add({severity:'info', 
          summary:'Vuelta atrás', 
          detail:`Has decidido no habilitar el usuario.`,
          life: 5000 });
      },
    });
  }
  
  btnUpdateUser(isUpdate: boolean, isCreate: boolean, id: number): void{
    this.router.navigate(['user/form'], {
      queryParams: {isUpdate: isUpdate, isCreate: isCreate, id: id}
    });
  }

  btnCreateUser(isUpdate: boolean, isCreate: boolean): void{
    this.router.navigate(['user/form'], {
      queryParams: {isUpdate: isUpdate, isCreate: isCreate}
    })
  }

  btnGenerarReporte(): void {
    this.messageService.add({severity:'info', 
      summary:'En Proceso', 
      detail:`Generando el reporte, puede tardar unos segundos...`,
      life: 5000 });
    this.userService.obtenerReporte().subscribe((blob: Blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'ReporteUsuario.pdf';
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
}
