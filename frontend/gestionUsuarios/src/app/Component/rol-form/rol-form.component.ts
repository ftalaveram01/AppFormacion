import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RolService } from '../../Services/rol.service';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-rol-form',
  templateUrl: './rol-form.component.html',
  styleUrls: ['./rol-form.component.css'],
    imports: [ReactiveFormsModule, CommonModule]
  
})
export class RolFormComponent implements OnInit {
  rolForm: FormGroup;
  succesCreate!: boolean;
  isUpdate: boolean = false;
  isCreate: boolean = false;
  succesUpdate!: boolean;
  idRol!: number;

  constructor(private rolService: RolService, private fb: FormBuilder, private router: Router, private route: ActivatedRoute) {
    this.rolForm = this.fb.group({

    })
   }

   ngOnInit(): void {
    this.rolForm = this.fb.group({
      nombreRol: [''],
      descripcion: ['', Validators.required]
    });
  
    this.route.queryParams.subscribe(params => {
      this.isUpdate = params['isUpdate'] === 'true';
      this.isCreate = params['isCreate'] === 'true';
      this.idRol = params['id'];
  
      if (this.isUpdate && this.idRol) {
        this.rolService.getRolById(this.idRol).subscribe(rol => {
          this.rolForm.patchValue(rol);
        });
      }
    });
  }
  

  private createRol(rol: any): void {
    this.rolService.createRol(rol).subscribe(response => {
      this.succesCreate = true;
      this.rolForm.reset();
      alert('El rol fue correctamente creado');
      this.router.navigate(['/rols']);
    });
  }

  private updateRol(id: number, rol: any): void {
    this.rolService.updateRol(id, rol).subscribe(response => {
      this.succesUpdate = true;
      this.rolForm.reset();
      alert('El rol fue correctamente actualizado');
      this.router.navigate(['/rols']);
    });
  }

  onSubmit(): void {
    if (this.rolForm.valid) {

      if(this.isCreate){
        this.createRol(this.rolForm.value)
      }

      if (this.isUpdate) {
        this.updateRol(this.idRol, this.rolForm.value);
      }
    }
  }
}
