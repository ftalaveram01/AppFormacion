import { Component } from '@angular/core';
import { ConvocatoriaService } from '../../Services/convocatoria.service';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-convocatoria-form',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './convocatoria-form.component.html',
  styleUrl: './convocatoria-form.component.css'
})
export class ConvocatoriaFormComponent {

  convocatoriaForm: FormGroup;
  idCurso!: Number;
  idConvocatoria!: Number;
  isUpdate!: boolean;
  isCreate!: boolean;

  constructor(private convocatoriaService: ConvocatoriaService, private fb: FormBuilder, private router: ActivatedRoute, private routerNav: Router) {
    this.convocatoriaForm = this.fb.group({
      fechaInicio: [''],
      fechaFin: ['']
    });
  }

  ngOnInit() {
    this.idCurso = Number(this.router.snapshot.queryParamMap.get('idCurso'));
    this.idConvocatoria = Number(this.router.snapshot.queryParamMap.get('idConvocatoria'))
    this.isUpdate = (this.router.snapshot.queryParamMap.get('isUpdate')) === 'true'
    this.isCreate = (this.router.snapshot.queryParamMap.get('isCreate')) === 'true'
  }

  btnConfirmarFechas(): any {

    const fechaInicio = this.convocatoriaForm.get('fechaInicio')?.value;
    const fechaFin = this.convocatoriaForm.get('fechaFin')?.value;

    if (this.isCreate) {
      const convocatoria = {
        "fechaInicio": this.fechaInicio?.value,
        "fechaFin": this.fechaFin?.value,
        "idCurso": this.idCurso
      }

      if (fechaFin < fechaInicio) {
        alert('La fecha fin debe ser posterior a la fecha inicio');
        return;
      } else {
        this.convocatoriaService.createConvocatoria(convocatoria).subscribe();
        alert('Convocatoria creada correctamente')
        this.routerNav.navigate(['cursos'])
      }
    } else {
      const convocatoria = {
        "fechaInicio": this.fechaInicio?.value.split('T')[0],
        "fechaFin": this.fechaFin?.value.split('T')[0]
      }

      this.convocatoriaService.updateConvocatoria(this.idConvocatoria, convocatoria).subscribe();
      this.routerNav.navigate(['convocatorias'])
    }
  }

  get fechaInicio() {
    return this.convocatoriaForm.get('fechaInicio')
  }

  get fechaFin() {
    return this.convocatoriaForm.get('fechaFin')
  }


}
