// angular components
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {FlexLayoutModule} from '@angular/flex-layout';
import { MatJumbotronModule } from '@angular-material-extensions/jumbotron';
// ngx
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

// own components
import { AboutComponent } from './about/about.component';
import { LegalComponent } from './legal/legal.component';
import { TermsComponent } from './terms/terms.component';
import { IncompatibleBrowserComponent } from './incompatible-browser/incompatible-browser.component';
import { CookiesDialogComponent } from './cookies-dialog/cookies-dialog.component';

// material
import { MatFormFieldModule } from '@angular/material/form-field';

import {
  MatAutocompleteModule,
  MatBadgeModule,
  MatBottomSheetModule,
  MatButtonModule,
  MatButtonToggleModule,
  MatCardModule,
  MatCheckboxModule,
  MatChipsModule,
  MatDatepickerModule,
  MatDialogModule,
  MatDividerModule,
  MatExpansionModule,
  MatGridListModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatRippleModule,
  MatSelectModule,
  MatSidenavModule,
  MatSliderModule,
  MatSlideToggleModule,
  MatSnackBarModule,
  MatSortModule,
  MatStepperModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule,
  MatTooltipModule,
  MatTreeModule,
} from '@angular/material';
import { CookieModule } from 'ngx-cookie';
import { ProgressComponent } from './progress/progress.component';
import { FooterComponent } from './footer/footer.component';
import { HeaderComponent } from './header/header.component';
import {LoginComponent} from './login/login.component';
import {CoreModule} from './core';
import { HomeComponent } from './home/home.component';
import {CommonModule} from '@angular/common';
import {ToastrModule} from 'ngx-toastr';
import {RegistrationModule} from './registration/registration.module';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './i18n/');
}

@NgModule({
  declarations: [
    AppComponent,
    IncompatibleBrowserComponent,
    AboutComponent,
    LegalComponent,
    TermsComponent,
    CookiesDialogComponent,
    LoginComponent,
    ProgressComponent,
    FooterComponent,
    HeaderComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    CookieModule.forRoot(),
    HttpClientModule,
    BrowserAnimationsModule,
    TranslateModule.forRoot(),

    // TranslateModule.forRoot({
    //   loader: {
    //     provide: TranslateLoader,
    //     useFactory: HttpLoaderFactory,
    //     deps: [HttpClient]
    //   },
    // }),
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatDialogModule,
    MatCheckboxModule,
    MatProgressSpinnerModule,
    MatProgressBarModule,
    MatSortModule,
    MatPaginatorModule,
    MatTooltipModule,
    MatTableModule,
    MatTabsModule,
    MatSelectModule,
    FormsModule,
    ReactiveFormsModule,
    AppRoutingModule,

    CoreModule,
    MatToolbarModule,
    FlexLayoutModule,
    MatJumbotronModule,
    MatGridListModule,
    MatMenuModule,
    MatCardModule,
    MatDividerModule,
    MatButtonModule,
    MatRadioModule,
    BrowserAnimationsModule,
    CommonModule,
    ToastrModule.forRoot(),
    RegistrationModule,
  ],
  exports: [
    MatAutocompleteModule,
    MatBadgeModule,
    MatBottomSheetModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatStepperModule,
    MatDatepickerModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatRippleModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
    MatTreeModule
  ],
  entryComponents: [CookiesDialogComponent],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
