import { Component, OnInit } from '@angular/core';
import { ConvocatoriaService } from '../../Services/convocatoria.service';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { DatePickerModule } from 'primeng/datepicker';
import { ButtonModule } from 'primeng/button';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-convocatoria-form',
  imports: [CommonModule, ReactiveFormsModule, DatePickerModule, ButtonModule],
  templateUrl: './convocatoria-form.component.html',
  styleUrls: ['./convocatoria-form.component.css']
})
export class ConvocatoriaFormComponent implements OnInit {

  convocatoriaForm: FormGroup;
  idCurso!: number;
  idConvocatoria!: number;
  isUpdate!: boolean;
  isCreate!: boolean;
  loading: boolean = false;

  constructor(
    private convocatoriaService: ConvocatoriaService,
    private fb: FormBuilder,
    private router: ActivatedRoute,
    private routerNav: Router
  ) {
    this.convocatoriaForm = this.fb.group({
      fechaInicio: ['', [Validators.required, this.validadorFecha]],
      fechaFin: ['', [Validators.required, this.validadorFecha]]
    }, { validator: this.validadorComparacionFechas });
  }

  ngOnInit() {
    this.idCurso = Number(this.router.snapshot.queryParamMap.get('idCurso'));
    this.idConvocatoria = Number(this.router.snapshot.queryParamMap.get('idConvocatoria'));
    this.isUpdate = (this.router.snapshot.queryParamMap.get('isUpdate')) === 'true';
    this.isCreate = (this.router.snapshot.queryParamMap.get('isCreate')) === 'true';
  }

  btnConfirmarFechas(): any {

    this.loading = true;
  
    const fechaInicio = this.convocatoriaForm.get('fechaInicio')?.value;
    const fechaFin = this.convocatoriaForm.get('fechaFin')?.value;
  
    if (this.isCreate) {
      const convocatoria = {
        fechaInicio,
        fechaFin,
        idCurso: this.idCurso
      };
  
      this.convocatoriaService.createConvocatoria(convocatoria).subscribe({
        next: () => {
          Swal.fire({
            title: "Perfecto",
            text: "Convocatoria creada correctamente",
            icon: "success",
            timer: 2000,
            willClose: () => {
              this.routerNav.navigate(['cursos']);
            }
          });
          this.loading = false;
        },
        error: (error) => {
          Swal.fire({
            title: "Algo ha ido mal",
            text: "Error al crear la convocatoria",
            icon: "error",
            timer: 2000,
            willClose: () => {
              this.loading = false;
            }
          });
        }
      });
    } else {
      const convocatoria = {
        fechaInicio: fechaInicio.split('T')[0],
        fechaFin: fechaFin.split('T')[0]
      };
  
      this.convocatoriaService.updateConvocatoria(this.idConvocatoria, convocatoria).subscribe({
        next: () => {
          Swal.fire({
            title: "Perfecto",
            text: "Convocatoria actualizada correctamente",
            icon: "success",
            timer: 2000,
            willClose: () => {
              this.routerNav.navigate(['convocatorias']);
            }
          });
          this.loading = false;
        },
        error: (error) => {
          Swal.fire({
            title: "Algo ha ido mal",
            text: "Error al actualizar la convocatoria",
            icon: "error",
            timer: 2000,
            willClose: () => {
              this.loading = false;
            }
          });
        }
      });
    }
  }
  

  validadorFecha(control: any): { [key: string]: boolean } | null {
    const fechaActual = new Date();
    const fechaInput = new Date(control.value);
    if (fechaInput <= fechaActual) {
      return { 'fechaInvalida': true };
    }
    return null;
  }

  validadorComparacionFechas(grupo: FormGroup): { [key: string]: boolean } | null {
    const fechaInicio = grupo.get('fechaInicio')?.value;
    const fechaFin = grupo.get('fechaFin')?.value;
    if (fechaInicio && fechaFin && fechaFin < fechaInicio) {
      return { 'desajusteFechas': true };
    }
    return null;
  }

  get fechaInicio() {
    return this.convocatoriaForm.get('fechaInicio');
  }

  get fechaFin() {
    return this.convocatoriaForm.get('fechaFin');
  }

  
  redirigirCursos() {
    this.routerNav.navigate(['/cursos']);
  }
    
}
