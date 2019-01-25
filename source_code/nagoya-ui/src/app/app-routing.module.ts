import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AboutComponent } from './components/about/about.component';
import { LegalComponent } from './components/legal/legal.component';
import { TermsComponent } from './components/terms/terms.component';
import { IncompatibleBrowserComponent } from './components/incompatible-browser/incompatible-browser.component';
import { LoginComponent } from './components/login/login.component';
import { AuthenticationGuard } from './core/authentication/authentication.guard';
import { HomeComponent } from './components/home/home.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { ConfirmationComponent } from './components/confirmation/confirmation.component';
import { ResourceComponent } from './components/resource/resource.component';
import { TradeContractComponent } from './components/trade-contract/trade-contract.component';
import { TradeOverviewComponent } from './components/trade-overview/trade-overview.component';
import { MasterDataComponent } from './components/master-data/master-data.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';

const routes: Routes = [
  { path: 'about', component: AboutComponent, },
  { path: 'legal', component: LegalComponent },
  { path: 'terms', component: TermsComponent, },
  { path: 'register', component: RegistrationComponent },
  { path: 'login', component: LoginComponent },
  { path: 'browsernotok', component: IncompatibleBrowserComponent },
  { path: 'home', component: HomeComponent, canActivate: [AuthenticationGuard] },
  { path: 'confirmation', component: ConfirmationComponent },
  { path: 'confirmation/:token', component: ConfirmationComponent },
  { path: 'resource', component: ResourceComponent, canActivate: [AuthenticationGuard] },
  { path: 'trade/contract', component: TradeContractComponent, canActivate: [AuthenticationGuard] },
  { path: 'trade/overview', component: TradeOverviewComponent, canActivate: [AuthenticationGuard] },
  { path: 'masterData', component: MasterDataComponent, canActivate: [AuthenticationGuard] },
  { path: 'change/password', component: ChangePasswordComponent, canActivate: [AuthenticationGuard] },
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  { path: '**', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
