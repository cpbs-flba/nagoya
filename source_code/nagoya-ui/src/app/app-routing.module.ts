import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AboutComponent } from './about/about.component';
import { LegalComponent } from './legal/legal.component';
import { TermsComponent } from './terms/terms.component';
import { IncompatibleBrowserComponent } from './incompatible-browser/incompatible-browser.component';
import { RegisterComponent } from './register/register.component';
import {LoginComponent} from './login/login.component';
import {AuthenticationGuard} from './core/authentication/authentication.guard';

const routes: Routes = [
  {path: 'about', component: AboutComponent, canActivate: [AuthenticationGuard]},
  {path: 'legal', component: LegalComponent},
  {path: 'terms', component: TermsComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'login', component: LoginComponent},
  {path: 'browsernotok', component: IncompatibleBrowserComponent},
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {path: '**', component: LoginComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
