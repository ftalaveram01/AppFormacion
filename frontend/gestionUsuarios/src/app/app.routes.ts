import { Routes } from '@angular/router';
import { InicioComponent } from './Component/inicio/inicio.component';
import { LoginComponent } from './Component/login/login.component';
import { RegisterComponent } from './Component/register/register.component';
import { CursoFormComponent } from './Component/curso-form/curso-form.component';
import { CursoListComponent } from './Component/curso-list/curso-list.component';
import { UserComponent } from './Component/user/user.component';
import { UserFormComponent } from './Component/user-form/user-form.component';
import { RolComponent } from './Component/rol/rol.component';
import { RolFormComponent } from './Component/rol-form/rol-form.component';
import { ConvocatoriaComponent } from './Component/convocatoria/convocatoria.component';
import { ConvocatoriaFormComponent } from './Component/convocatoria-form/convocatoria-form.component';
import { ConfirmacionComponent } from './Component/confirmacion/confirmacion.component';
import { AuthGuard } from './auth.guard';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'inicio', component: InicioComponent, canActivate: [AuthGuard] },
  { path: 'cursos', component: CursoListComponent, canActivate: [AuthGuard] },
  { path: 'cursos/form', component: CursoFormComponent, canActivate: [AuthGuard] },
  { path: 'users', component: UserComponent, canActivate: [AuthGuard] },
  { path: 'user/form', component: UserFormComponent, canActivate: [AuthGuard] },
  { path: 'rols', component: RolComponent, canActivate: [AuthGuard] },
  { path: 'rols/form', component: RolFormComponent, canActivate: [AuthGuard] },
  { path: 'convocatorias', component: ConvocatoriaComponent, canActivate: [AuthGuard] },
  { path: 'convocatorias/form', component: ConvocatoriaFormComponent, canActivate: [AuthGuard] },
  { path: 'confirmacion', component: ConfirmacionComponent, canActivate: [AuthGuard] }
];
