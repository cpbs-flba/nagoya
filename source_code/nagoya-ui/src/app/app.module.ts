// angular components
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule, HttpClient} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {FlexLayoutModule} from '@angular/flex-layout';
import {MatJumbotronModule} from '@angular-material-extensions/jumbotron';
// ngx
import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';

// own components
import {AboutComponent} from './about/about.component';
import {LegalComponent} from './legal/legal.component';
import {TermsComponent} from './terms/terms.component';
import {IncompatibleBrowserComponent} from './incompatible-browser/incompatible-browser.component';
import {CookiesDialogComponent} from './cookies-dialog/cookies-dialog.component';

// material
import {MatFormFieldModule} from '@angular/material/form-field';

import {
  DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE,
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
import {CookieModule} from 'ngx-cookie';
import {ProgressComponent} from './progress/progress.component';
import {FooterComponent} from './footer/footer.component';
import {HeaderComponent} from './header/header.component';
import {LoginComponent} from './login/login.component';
import {CoreModule} from './core';
import {HomeComponent} from './home/home.component';
import {CommonModule} from '@angular/common';
import {ToastrModule} from 'ngx-toastr';
import {RegistrationComponent} from './registration/registration.component';
import {DynamicFormComponent} from './registration/dynamic-form/dynamic-form.component';
import {ConfirmationComponent} from './confirmation/confirmation.component';
import {MatMomentDateModule} from '@angular/material-moment-adapter';
import { ResourceComponent } from './resource/resource.component';
import { CreationComponent } from './resource/creation/creation.component';
import { TemplateComponent } from './resource/template/template.component';
import { MasterDataComponent } from './master-data/master-data.component';
import { TradeOfferComponent } from './trade-offer/trade-offer.component';
import { TradeOverviewComponent } from './trade-overview/trade-overview.component';
import { TradeContractComponent } from './trade-contract/trade-contract.component';
import { ChangePasswordComponent } from './change-password/change-password.component';

// import {RegistrationModule} from './registration/registration.module';

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
    HomeComponent,
    RegistrationComponent,
    DynamicFormComponent,
    ConfirmationComponent,
    ResourceComponent,
    CreationComponent,
    TemplateComponent,
    MasterDataComponent,
    TradeOfferComponent,
    TradeOverviewComponent,
    TradeContractComponent,
    ChangePasswordComponent
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
    MatSidenavModule
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
  providers: [{provide: MAT_DATE_LOCALE, useValue: 'de-AT'}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
