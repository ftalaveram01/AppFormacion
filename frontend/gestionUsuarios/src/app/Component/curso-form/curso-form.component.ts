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
  searchForm: FormGroup;

  isUpdate: boolean = false;
  idCurso!: number;
  succesUpdate!: boolean;
  succesCreate!: boolean;
  usuarios: any[] = [];
  seleccionadosForm: FormGroup;

  // PAGINACION DE USUARIOS

  seleccionados: Set<number> = new Set();
  usuariosMostrados: any[] = []
  page: number = 1;
  limiteUsuarios: number = 5;
  totalPages: number = 0;

  constructor(private cursoService: CursoService, private fb: FormBuilder, private route: ActivatedRoute, private router: Router){
    this.cursoForm = this.fb.group({
      nombre: [''],
      descripcion: [''],
      fechaInicio: [''],
      fechaFin: [''],
      router:['']
    });

    this.searchForm = this.fb.group({
      userSearch: ['']
    })

    this.seleccionadosForm = this.fb.group({});

  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.isUpdate = params['isUpdate']
      
      //PARA TRAER TODO LOS USUARIOS AL ACTUALIZAR UN CURSO
      if (params['id']) {
        this.idCurso = +params['id'];
        this.cursoService.getCurso(this.idCurso).subscribe(curso => {
          this.cursoForm.patchValue(curso)
  
          const fechaInicio = new Date(curso.fechaInicio);
          const fechaFin = new Date(curso.fechaFin)
   
          this.cursoForm.patchValue({
            fechaInicio: this.formatearFecha(fechaInicio)
          });
          this.cursoForm.patchValue({
            fechaFin: this.formatearFecha(fechaFin)
          });
          this.getUsuarios(curso);

        })

      }else{
        //PARA TRAER TODOS LOS USUARIOS AL CREAR UN CURSO
        this.getUsuarios(null)
      }
    })
    // PAGINACION
    this.getUsersPage();
  }
  
  getUsuarios(curso: any = null): void {
    this.cursoService.getUsuarios(this.usuarios).subscribe(response => {
      this.usuarios = response;

      if (curso) {
        const usuariosCurso = curso.usuarios.map((u: any) => u.id);
        const grupo = this.fb.group({});
        this.usuarios.forEach((usuario,i) => {
          const seleccionado = curso ? usuariosCurso.includes(usuario.id) : false;
          grupo.addControl(`seleccionado${usuario.id}`, this.fb.control(seleccionado));
          console.log(seleccionado)
        });
        this.seleccionadosForm = grupo;
      }else{
          const grupo = this.fb.group({});
          this.usuarios.forEach((usuario, i) => {
            grupo.addControl(`seleccionado${usuario.id}`, this.fb.control(false));
          });
          this.seleccionadosForm = grupo;
   
      }   
    })
  
  }
  
  onSearch(): void {
    const searchTerm = this.searchForm.get('userSearch')?.value.toLowerCase();
    const filteredUsuarios = this.usuarios.filter(usuario =>
      usuario.email.includes(searchTerm)
    );
    this.paginar(filteredUsuarios);
  }

  onSubmit(): void{
    if(this.cursoForm.valid){
      const usuariosSeleccionados = this.usuarios.filter(usuario =>
        this.seleccionadosForm.get(`seleccionado${usuario.id}`)?.value
      );
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
 
  private formatearFecha(fechaInicio: Date) : String{
    const anio = fechaInicio.getFullYear();
    const mes = (fechaInicio.getMonth() + 1).toString().padStart(2, '0');
    const dia = fechaInicio.getDate().toString().padStart(2, '0');
    const hora = fechaInicio.getHours().toString().padStart(2, '0');
    const minutos = fechaInicio.getMinutes().toString().padStart(2, '0');
   
    const fechaFormateada = `${anio}-${mes}-${dia}T${hora}:${minutos}`;
   
    return fechaFormateada
  }

  // PAGINACION

  private paginar(users: any[]){
    const start = (this.page -1) * this.limiteUsuarios;
    const end = start + this.limiteUsuarios;
    
    this.usuariosMostrados = users.slice(start,end)
    console.log(this.usuariosMostrados)
  }

  private getUsersPage(){
    this.cursoService.getUsuarios(this.usuarios).subscribe(response => {

      this.totalPages = Math.ceil(response.length / this.limiteUsuarios);
      this.paginar(response);

    });
  }

  nextPage(users: any[]){
    if(this.page < this.totalPages){
      this.page++;
      this.getUsersPage();
    }
  }

  prevPage(users: any[]){
    if(this.page > 1){
      this.page-- ;
      this.getUsersPage();
    }
  }
}
