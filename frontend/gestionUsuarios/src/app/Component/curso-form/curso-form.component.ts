import { Component, OnInit } from '@angular/core';
import { CursoService } from '../../Services/curso.service';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-curso-form',
  imports: [ReactiveFormsModule],
  templateUrl: './curso-form.component.html',
  styleUrl: './curso-form.component.css'
})
export class CursoFormComponent implements OnInit{

  cursoForm: FormGroup;
  isUpdate: boolean = false;
  idCurso!: number;

  constructor(private cursoService: CursoService, private fb: FormBuilder, private route: ActivatedRoute){
    this.cursoForm = this.fb.group({
      nombre: [''],
      descripcion: [''],
      fechaInicio: [''],
      fechaFin: ['']
    });
  }

  ngOnInit(): void {

    this.route.queryParams.subscribe(params =>{
      this.isUpdate = params['isUpdate']
      if(params['id']){
        this.idCurso = +params['id'];
        this.cursoService.getCurso(this.idCurso).subscribe(curso =>{
          this.cursoForm.patchValue(curso)
        })
      }
    })

  }

  onSubmit(): void{
    if(this.cursoForm.valid){
      if(this.isUpdate){
        this.updateCurso(this.idCurso,this.cursoForm.value);
      }else{
        this.createCurso(this.cursoForm.value);
      }
    }
  }

  private updateCurso(id: number, curso: any): void{
    this.cursoService.updateCurso(id, curso ).subscribe(response => {
      console.log('Curso actualizado:');
      this.cursoForm.reset();
    },)
  }

  private createCurso(curso: any): void{
    this.cursoService.createCurso(curso).subscribe(response => {
      console.log('Curso creado:');
      this.cursoForm.reset();
    },)
  }

}
