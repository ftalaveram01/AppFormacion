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


export const routes: Routes = [
    { path: '', component: LoginComponent},
    { path: 'login', component: LoginComponent},
    { path: 'register', component: RegisterComponent},
    { path: 'inicio', component: InicioComponent},
    { path: 'cursos', component: CursoListComponent },
    { path: 'cursos/form', component: CursoFormComponent },
    { path: `users`, component: UserComponent},
    { path: `user/form`, component: UserFormComponent},
    { path: `rols`, component: RolComponent},
    { path: `rols/form`, component: RolFormComponent},
    { path: `convocatorias`, component: ConvocatoriaComponent},
    { path: `convocatorias/form`, component: ConvocatoriaFormComponent},
];