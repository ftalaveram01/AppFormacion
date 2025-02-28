import { Component, OnInit } from '@angular/core';
import { CursoService } from '../../Services/curso.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
CursoService

@Component({
  selector: 'app-curso-list',
  imports: [CommonModule],
  templateUrl: './curso-list.component.html',
  styleUrl: './curso-list.component.css'
})
export class CursoListComponent implements OnInit{

   cursos: any[] = [];
   idAdmin!: number;

  constructor(private cursoService: CursoService, private router: Router) {}

  ngOnInit(): void {
    this.cursoService.getCursos().subscribe(data => {
      this.cursos = data;
    });

    this.idAdmin = Number(localStorage.getItem('idAdmin'))
  }

  deleteCurso(id: number): void{
    this.cursoService.deleteCurso(id).subscribe(() => {
      this.cursos = this.cursos.filter(c => c.id !== id);
    })
  }

  btnUpdateCurso(isUpdate: boolean, id: number): void{
    this.router.navigate(['cursos/form'], {
      queryParams: {isUpdate: isUpdate, id: id}
    });
  }
  
  btnMatricularse(idCurso: number): void{
    const idUsuario = Number(localStorage.getItem('idUsuario'))
    this.cursoService.addUsuarioToCurso(idCurso, idUsuario).subscribe(() => {
    })

    this.cursoService.getCursos().subscribe(data => {
      this.cursos = data;
    });
  }

  btnDesmatricularse(idCurso: number): void{
    const idUsuario = Number(localStorage.getItem('idUsuario'))
    this.cursoService.deleteUsuarioFromCurso(idUsuario, idCurso).subscribe(() => {
      this.cursoService.getCursos().subscribe(data => {
        this.cursos = data;
      });
    })
  }

  btnDeleteUserCourse(id: number, idCurso: number): void{
 
    this.cursoService.deleteUsuarioFromCurso(id, idCurso).subscribe(() => {
      this.cursos = this.cursos.filter( u => u.id !== id)
      this.cursoService.getCursos().subscribe(data => {
        this.cursos = data;
      });
    })
  }
  

  isInscrito(idCurso: number): boolean{
    const curso = this.cursos.find(c => c.id === idCurso);
    if(!curso)
      return false;
    const idUsuario = Number(localStorage.getItem('idUsuario'));

    return curso.usuarios.some((u: any) => u.id === idUsuario);
  }

  borrarCache(): void {
    localStorage.clear();
  }
}


