import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { RolService } from '../../Services/rol.service';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';

@Component({
  selector: 'app-rol',
  imports: [CommonModule, ReactiveFormsModule, ToastModule, ConfirmDialogModule],
  templateUrl: './rol.component.html',
  styleUrl: './rol.component.css',
  providers: [MessageService, ConfirmationService]
})
export class RolComponent {

  roles: any[] = [];
  idAdmin!: number;

  formulario!: FormGroup
  resultado: any[] = []
  filtrado: boolean = false

  constructor(
    private rolService: RolService, 
    private router: Router,
    private messageService: MessageService,
    private confirmationService: ConfirmationService) {

  }

  btnCreateRol(isUpdate: boolean, isCreate: boolean): void{
    this.router.navigate(['rols/form'], {
      queryParams: {isUpdate: isUpdate, isCreate: isCreate}
    })
  }

  btnUpdateRol(isUpdate: boolean, isCreate: boolean, id: number): void{
    this.router.navigate(['rols/form'], {
      queryParams: {isUpdate: isUpdate, isCreate: isCreate, id: id}
    });
  }

  btnDeleteRol(id: number): void{
    this.rolService.deleteRol(id).subscribe(() => {
      this.roles = this.roles.filter( r => r.id !== id);
    });
  }

  btnGenerarReporte(): void {
    this.messageService.add({severity:'info', 
      summary:'En Proceso', 
      detail:`Generando el reporte, puede tardar unos segundos...`,
      life: 5000 });
    this.rolService.obtenerReporte().subscribe((blob: Blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'ReporteRol.pdf';
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


  deleteRol(id: number): void{
    this.rolService.deleteRol(id).subscribe(() =>{
      this.roles = this.roles.filter(r => r.id !== id);
    })
  }

  buscarRol(): void{

    this.resultado = this.roles.filter(rol => {
      return rol.nombreRol.toLowerCase().includes(this.formulario.value.nombreRol)
    })
    this.filtrado = true
  }
  
}
