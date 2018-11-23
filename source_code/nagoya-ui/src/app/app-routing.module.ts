import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AboutComponent } from './about/about.component';
import { LegalComponent } from './legal/legal.component';
import { TermsComponent } from './terms/terms.component';
import { IncompatibleBrowserComponent } from './incompatible-browser/incompatible-browser.component';

const routes: Routes = [
  {path: 'about', component: AboutComponent},
  {path: 'legal', component: LegalComponent},
  {path: 'terms', component: TermsComponent},
  {path: 'browsernotok', component: IncompatibleBrowserComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
