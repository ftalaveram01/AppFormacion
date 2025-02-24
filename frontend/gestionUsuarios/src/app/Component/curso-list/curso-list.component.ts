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
   idAdmin!: Number

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

}


