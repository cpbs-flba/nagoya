// angular components
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { FlexLayoutModule } from '@angular/flex-layout';
import { MatJumbotronModule } from '@angular-material-extensions/jumbotron';
import { CommonModule } from '@angular/common';
// ngx
import { ToastrModule } from 'ngx-toastr';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { CookieModule } from 'ngx-cookie';

// own components
import { ProgressComponent } from './components/progress/progress.component';
import { FooterComponent } from './components/footer/footer.component';
import { HeaderComponent } from './components/header/header.component';
import { LoginComponent } from './components/login/login.component';
import { CoreModule, TokenInterceptor } from './core';
import { HomeComponent } from './components/home/home.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { DynamicFormComponent } from './components/registration/dynamic-form/dynamic-form.component';
import { ConfirmationComponent } from './components/confirmation/confirmation.component';
import { MatMomentDateModule } from '@angular/material-moment-adapter';
import { ResourceComponent } from './components/resource/resource.component';
import { CreationComponent } from './components/resource/creation/creation.component';
import { MasterDataComponent } from './components/master-data/master-data.component';
import { TradeOverviewComponent } from './components/trade-overview/trade-overview.component';
import { TradeContractComponent } from './components/trade-contract/trade-contract.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { OverviewComponent } from './components/resource/overview/overview.component';
import { AboutComponent } from './components/about/about.component';
import { LegalComponent } from './components/legal/legal.component';
import { TermsComponent } from './components/terms/terms.component';
import { IncompatibleBrowserComponent } from './components/incompatible-browser/incompatible-browser.component';
import { CookiesDialogComponent } from './components/cookies-dialog/cookies-dialog.component';

// material
import { MatFormFieldModule } from '@angular/material/form-field';

// interceptor

import {
  MAT_DATE_LOCALE,
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
import { BlockchainExplorerComponent } from './components/blockchain-explorer/blockchain-explorer.component';


// import {RegistrationModule} from './registration/registration.module';

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/');
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
    HomeComponent,
    RegistrationComponent,
    DynamicFormComponent,
    ConfirmationComponent,
    ResourceComponent,
    CreationComponent,
    OverviewComponent,
    MasterDataComponent,
    TradeOverviewComponent,
    TradeContractComponent,
    ChangePasswordComponent,
    BlockchainExplorerComponent
  ],

  imports: [
    BrowserModule,
    CookieModule.forRoot(),
    HttpClientModule,
    BrowserAnimationsModule,


    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      },
    }),
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
    MatListModule,
    MatCardModule,
    MatDividerModule,
    MatButtonModule,
    MatRadioModule,
    BrowserAnimationsModule,
    CommonModule,
    ToastrModule.forRoot(),
    MatMomentDateModule,
    MatIconModule,
    MatExpansionModule,
    MatSidenavModule,
    MatAutocompleteModule
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
  providers: [
    {
      provide: MAT_DATE_LOCALE,
      useValue: 'de-DE'
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }],
  bootstrap: [AppComponent]
})
export class AppModule {
}
