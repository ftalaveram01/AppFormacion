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
  seleccionadosForm: FormGroup;

  seleccionados: Set<number> = new Set();
  usuariosMostrados: any[] = []
  page: number = 1;
  limiteUsuarios: number = 5;
  totalPages: number = 0;

  constructor(private convocatoriaService: ConvocatoriaService, private fb: FormBuilder, private router: ActivatedRoute, private routerNav: Router) {
    this.convocatoriaForm = this.fb.group({
      fechaInicio: [''],
      fechaFin: ['']
    });

    this.seleccionadosForm = this.fb.group({});
  }

  ngOnInit() {
    this.idCurso = Number(this.router.snapshot.queryParamMap.get('idCurso'));
    this.idConvocatoria = Number(this.router.snapshot.queryParamMap.get('idConvocatoria'))
    this.isUpdate = (this.router.snapshot.queryParamMap.get('isUpdate')) === 'true'
    this.isCreate = (this.router.snapshot.queryParamMap.get('isCreate')) === 'true'

    this.getUsersPage(this.idConvocatoria);
  }

  btnConfirmarFechas(): any{

    const fechaInicio = this.convocatoriaForm.get('fechaInicio')?.value;
    const fechaFin = this.convocatoriaForm.get('fechaFin')?.value;

    const convocatoria = {
      "fechaInicio": this.fechaInicio?.value.split('T')[0],
      "fechaFin": this.fechaFin?.value.split('T')[0],
      "idCurso": this.idCurso
    }

    this.convocatoriaService.createConvocatoria(convocatoria).subscribe();

    if (fechaFin < fechaInicio) {
      alert('La fecha fin debe ser posterior a la fecha inicio');
      return;
    }else{
      this.convocatoriaService.createConvocatoria(convocatoria).subscribe();
      alert('Convocatoria creada correctamente')
      //this.routerNav.navigate(['cursos'])
    }
  }

  get fechaInicio() {
    return this.convocatoriaForm.get('fechaInicio')
  }

  get fechaFin() {
    return this.convocatoriaForm.get('fechaFin')
  }

  // PAGINACION

  private paginar(users: any[]) {
    const start = (this.page - 1) * this.limiteUsuarios;
    const end = start + this.limiteUsuarios;

    this.usuariosMostrados = users.slice(start, end)
    console.log(this.usuariosMostrados)
  }

  private getUsersPage(idConvocatoria: Number) {
    this.convocatoriaService.getConvocatorias().subscribe(async data => {
      const usuarios = await data.find((convocatoria: { id: any; }) => {
        convocatoria.id === idConvocatoria
      }).usuarios;
      this.totalPages = Math.ceil(usuarios.length / this.limiteUsuarios);
      this.paginar(usuarios);
    })

  }

  nextPage(users: any[]) {
    if (this.page < this.totalPages) {
      this.page++;
      this.getUsersPage(this.idConvocatoria);
    }
  }

  prevPage(users: any[]) {
    if (this.page > 1) {
      this.page--;
      this.getUsersPage(this.idConvocatoria);
    }
  }
}
