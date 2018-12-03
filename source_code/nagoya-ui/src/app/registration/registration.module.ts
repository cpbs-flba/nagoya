import { NgModule } from '@angular/core';
import {CommonModule, NgSwitch} from '@angular/common';
import { RegistrationComponent } from './registration.component';
import {DynamicFormComponent} from './dynamic-form/dynamic-form.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {
  MatButtonModule, MatCardModule, MatDividerModule, MatFormFieldModule, MatInputModule,
  MatRadioModule
} from '@angular/material';
import {CoreModule} from '../core';
import {I18nService} from '../services/i18n.service';
import {TranslateModule} from '@ngx-translate/core';
import {FlexLayoutModule} from '@angular/flex-layout';
import {HttpClientModule} from '@angular/common/http';
import {RegistrationService} from '../services/registration.service';

@NgModule({
  declarations: [RegistrationComponent, DynamicFormComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    BrowserModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatCardModule,
    MatRadioModule,
    MatDividerModule,
    CoreModule,
    TranslateModule.forRoot(),
    FormsModule,
    FlexLayoutModule,
    HttpClientModule,
    // RegistrationService

  ],
  bootstrap: [RegistrationComponent]
})
export class RegistrationModule { }
