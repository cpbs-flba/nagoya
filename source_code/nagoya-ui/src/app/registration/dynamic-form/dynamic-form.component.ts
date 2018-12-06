import {Component, Input, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {RegistrationService} from '../../services/registration.service';
import {Router} from '@angular/router';
import {MessageService} from '../../services/message.service';

@Component({
  selector: 'app-dynamic-form',
  templateUrl: './dynamic-form.component.html',
  styleUrls: ['./dynamic-form.component.scss']
})
export class DynamicFormComponent implements OnInit {

  @Input() dataObject;
  @Input() selectedPersonType;
  public form: FormGroup;
  public objectProps;
  maxDate: Date;
  minDate: Date = new Date('1900-01-01');

  constructor(private registrationService: RegistrationService,
              private router: Router,
              private messageService: MessageService) {

    this.maxDate = new Date();
    this.maxDate.setFullYear(new Date().getFullYear() - 18);
  }

  ngOnInit() {
    // remap the API to be suitable for iterating over it
    this.objectProps =
      Object.keys(this.dataObject)
        .map(prop => {
          return Object.assign({}, {key: prop}, this.dataObject[prop]);
        });

    // setup the form
    const formGroup = {};
    for (const prop of Object.keys(this.dataObject)) {
      formGroup[prop] = new FormControl(this.dataObject[prop].value || '', this.mapValidators(this.dataObject[prop].validation));
    }
    // this.minDate =
    this.form = new FormGroup(formGroup);

  }

  private mapValidators(validators) {
    const formValidators = [];

    if (validators) {
      for (const validation of Object.keys(validators)) {
        if (validation === 'required') {
          formValidators.push(Validators.required);
        } else if (validation === 'min') {
          formValidators.push(Validators.min(validators[validation]));
        }
      }
    }

    return formValidators;
  }

  onSubmit() {
    console.log(this.form.value, this.selectedPersonType);
    this.registrationService.register(this.form.value, this.selectedPersonType)
      .subscribe(response => {
        this.router.navigate(['confirmation']);

      }, error => {

        if (error.status === 409) {
          this.messageService.displayErrorMessage('CONFIRMATION.ERROR.EMAIL_ALREADY_IN_USE');
        } else {
          this.messageService.displayErrorMessage('REGISTRATION.FORM.ERROR.REGISTRATION_FAILED');
        }
      });
  }


}
