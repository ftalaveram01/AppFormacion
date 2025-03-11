import { Component, OnInit } from '@angular/core';
import { CursoService } from '../../Services/curso.service';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-curso-form',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './curso-form.component.html',
  styleUrl: './curso-form.component.css'
})
export class CursoFormComponent implements OnInit{

  cursoForm: FormGroup;
  isUpdate: boolean = false;
  idCurso!: number;
  succesUpdate!: boolean;
  succesCreate!: boolean;
  usuarios: any[] = [];
  seleccionadosForm: FormGroup;




  constructor(private cursoService: CursoService, private fb: FormBuilder, private route: ActivatedRoute, private router: Router){
    this.cursoForm = this.fb.group({
      nombre: [''],
      descripcion: [''],
      numeroHoras: ['']
    });
    this.seleccionadosForm = this.fb.group({});

  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.isUpdate = params['isUpdate']
      if (params['id']) {
        this.idCurso = +params['id'];
        this.cursoService.getCurso(this.idCurso).subscribe(curso => {
          this.cursoForm.patchValue(curso)
          this.getUsuarios(curso);
        })
      } else {
        this.getUsuarios(null);
      }
    })
  }
  
  getUsuarios(curso: any = null): void {
    this.cursoService.getUsuarios(this.usuarios).subscribe(response => {
      this.usuarios = response;
      if (curso) {
        const usuariosCurso = curso.usuarios.map((u: any) => u.id);
        const grupo = this.fb.group({});
        this.usuarios.forEach((usuario, i) => {
          const seleccionado = usuariosCurso.includes(usuario.id);
          grupo.addControl(`seleccionado${i}`, this.fb.control(seleccionado));
        });
        this.seleccionadosForm = grupo;
      } else {
        const grupo = this.fb.group({});
        this.usuarios.forEach((usuario, i) => {
          grupo.addControl(`seleccionado${i}`, this.fb.control(false));
        });
        this.seleccionadosForm = grupo;
      }
    })
  }
  

  onSubmit(): void{
    if(this.cursoForm.valid){
      const usuariosSeleccionados: any[] = [];
      Object.keys(this.seleccionadosForm.controls).forEach(key => {
        if (this.seleccionadosForm.get(key)?.value) {
          const index = parseInt(key.replace('seleccionado', ''));
          usuariosSeleccionados.push(this.usuarios[index]);
        }
      });
      if(this.isUpdate){
        this.updateCurso(this.idCurso,this.cursoForm.value, usuariosSeleccionados);
      }else{
        this.createCurso(this.cursoForm.value, usuariosSeleccionados);
      }
    }
  }

  private updateCurso(id: number, curso: any, usuariosSeleccionados: any[]): void {
    this.cursoService.updateCurso(id, curso, usuariosSeleccionados).subscribe(response => {
      console.log('Curso actualizado:');
  
      this.succesUpdate = true
  
      this.cursoForm.reset();
      alert('El curso fue correctamente actualizado')
      this.router.navigate(['/cursos'])
    },)
  }

  private createCurso(curso: any, usuariosSeleccionados: any[]): void {
    this.cursoService.createCurso(curso, usuariosSeleccionados).subscribe(response => {
      console.log('Curso creado:');
  
      this.succesCreate = true
  
      this.cursoForm.reset();
      alert('El curso fue correctamente creado')
      this.router.navigate(['/cursos'])
    },)
  }

}
