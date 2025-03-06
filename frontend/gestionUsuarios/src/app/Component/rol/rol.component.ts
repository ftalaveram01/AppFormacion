import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RolService } from '../../Services/rol.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-rol',
  imports: [CommonModule],
  templateUrl: './rol.component.html',
  styleUrl: './rol.component.css'
})
export class RolComponent {

  roles: any[] = [];

  constructor(private rolService:RolService, private router: Router) {}

  ngOnInit(): void{
    this.rolService.getRoles().subscribe((data) => {
      this.roles = data;
    })
  }

  borrarCache(): void {
    localStorage.clear();
  }
}
