import { Routes, RouterModule } from '@angular/router';
import { InicioComponent } from './Component/inicio/inicio.component';
import { LoginComponent } from './Component/login/login.component';
import { RegisterComponent } from './Component/register/register.component';

export const routes: Routes = [
    { path: '', component: LoginComponent},
    { path: 'login', component: LoginComponent},
    { path: 'register', component: RegisterComponent},
    { path: 'inicio', component: InicioComponent}
];