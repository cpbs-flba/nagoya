import {Component, OnInit} from '@angular/core';
import {naturalPerson} from './Models/naturalPerson';
import {legalPerson} from './Models/legalPerson';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {User} from '../model/user';
import {tap} from 'rxjs/internal/operators';
import {RegistrationService} from './registration.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent {
  naturalPerson;
  legalPerson;
  selectedPersonType: string;
  personTypes: string[] = ['natural', 'legal'];

  constructor(private registrationService: RegistrationService) {
    this.naturalPerson = naturalPerson;
    this.legalPerson = legalPerson;
  }

  public submitForm(form) {
    this.registrationService.register(form);

  }
}
