import {Component, OnInit} from '@angular/core';
import {legalPerson} from './models/legalPerson';
import {naturalPerson} from './models/naturalPerson';
import {RegistrationService} from '../services/registration.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  naturalPerson;
  legalPerson;
  selectedPersonType: string;
  personTypes: string[] = ['natural', 'legal'];

  constructor() {
    this.naturalPerson = naturalPerson;
    this.legalPerson = legalPerson;
  }

  ngOnInit() {
  }

}
