import { Component } from '@angular/core';
import { ConvocatoriaService } from '../../Services/convocatoria.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-confirmacion',
  imports: [CommonModule],
  templateUrl: './confirmacion.component.html',
  styleUrl: './confirmacion.component.css'
})
export class ConfirmacionComponent {

  idUsuario!: Number;
  idConvocatoria!: Number;

  constructor(private convocatoriaService: ConvocatoriaService, private aRouter: ActivatedRoute) { }

  ngOnInit() {
    this.idUsuario = Number(this.aRouter.snapshot.queryParamMap.get('idUsuario'))
    this.idConvocatoria = Number(this.aRouter.snapshot.queryParamMap.get('idConvocatoria'))
  }

  btnConfirmarAsistencia() {
    this.convocatoriaService.inscribirEnConvocatoria(this.idConvocatoria, this.idUsuario).subscribe();
  }
}
