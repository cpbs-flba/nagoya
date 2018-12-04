import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AboutComponent } from './about/about.component';
import { LegalComponent } from './legal/legal.component';
import { TermsComponent } from './terms/terms.component';
import { IncompatibleBrowserComponent } from './incompatible-browser/incompatible-browser.component';
import {LoginComponent} from './login/login.component';
import {AuthenticationGuard} from './core/authentication/authentication.guard';
import {HomeComponent} from './home/home.component';
import {RegistrationComponent} from './registration/registration.component';
import {ConfirmationComponent} from './confirmation/confirmation.component';

const routes: Routes = [
  {path: 'about', component: AboutComponent, },
  {path: 'legal', component: LegalComponent},
  {path: 'terms', component: TermsComponent, },
  {path: 'register', component: RegistrationComponent},
  {path: 'login', component: LoginComponent},
  {path: 'browsernotok', component: IncompatibleBrowserComponent},
  {path: 'home', component: HomeComponent, canActivate: [AuthenticationGuard]},
  {path: 'confirmation', component: ConfirmationComponent},
  {path: 'confirmation/:tokena', component: ConfirmationComponent},
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
