import { Component, OnInit } from '@angular/core';
import { ConvocatoriaService } from '../../Services/convocatoria.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-confirmacion',
  imports: [CommonModule],
  templateUrl: './confirmacion.component.html',
  styleUrls: ['./confirmacion.component.css']
})
export class ConfirmacionComponent implements OnInit {

  idUsuario!: number;
  idConvocatoria!: number;
  userIdFromToken!: number;

  constructor(
    private convocatoriaService: ConvocatoriaService,
    private aRouter: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.idUsuario = Number(this.aRouter.snapshot.queryParamMap.get('idUsuario'));
    this.idConvocatoria = Number(this.aRouter.snapshot.queryParamMap.get('idConvocatoria'));

    // Retrieve and decode the JWT
    const storedToken = localStorage.getItem('token');
    if (storedToken) {
      const token = JSON.parse(storedToken).token;
      const payload = this.decodeToken(token);
      this.userIdFromToken = Number(payload.sub);

      if (this.userIdFromToken !== this.idUsuario) {
        this.router.navigate(['/inicio']);
      }
    } else {
      this.router.navigate(['/login']);
    }
  }

  decodeToken(token: string): any {
    const payloadBase64 = token.split('.')[1];
    const payloadJson = atob(payloadBase64);
    return JSON.parse(payloadJson);
  }

  btnConfirmarAsistencia() {
    this.convocatoriaService.inscribirEnConvocatoria(this.idConvocatoria, this.idUsuario).subscribe({
      next: (data) => {
        this.router.navigate(['/inicio']);
      },
      error: (error) => {
        alert('Error al inscribirse en la convocatoria. Por favor, inténtelo de nuevo más tarde.');
        this.router.navigate(['/inicio']);
      }
    });
  }
}
