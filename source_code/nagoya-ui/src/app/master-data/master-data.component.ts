import {Component, OnInit} from '@angular/core';
import {UserService} from '../services/user.service';
import {FormBuilder, FormGroup, FormGroupName, Validators} from '@angular/forms';
import {MasterDataService} from '../services/master-data.service';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-master-data',
  templateUrl: './master-data.component.html',
  styleUrls: ['./master-data.component.scss']
})
export class MasterDataComponent implements OnInit {

  user: Person;
  toggleUpdateData = true;
  masterDataForm: FormGroup;

  constructor(private userService: UserService,
              private masterDataService: MasterDataService,
              private formBuilder: FormBuilder,
              public translate: TranslateService) {
  }

  ngOnInit() {
    this.user = this.userService.getUser();
    this.createMasterDataForm();
  }

  toggleEditMode() {
    this.toggleUpdateData = !this.toggleUpdateData;
  }

  createMasterDataForm() {
    let naturalPerson;
    let legalPerson;

    if (this.user.personType === 'NATURAL') {
      naturalPerson = <NaturalPerson> this.user;
    } else {
      legalPerson = <LegalPerson> this.user;
    }
    this.masterDataForm = this.formBuilder.group({
      firstname: [naturalPerson ? naturalPerson.firstname : '', naturalPerson ? Validators.required : {}],
      lastname: [naturalPerson ? naturalPerson.lastname : '', naturalPerson ? Validators.required : {}],
      name: [legalPerson ? legalPerson.name : '', legalPerson ? Validators.required : {}],
      taxNumber: [legalPerson ? legalPerson.taxNumber : '', legalPerson ? Validators.required : {}],
      commercialRegisterNumber: [legalPerson ? legalPerson.commercialRegisterNumber : '', legalPerson ? Validators.required : {}],
      email: [this.user.email, Validators.required],
      passwordConfirmation: ['', Validators.required]
    });
  }

  updateData() {
    this.user.email = this.masterDataForm.controls.email.value;
    this.user.passwordConfirmation = this.masterDataForm.controls.passwordConfirmation.value;
    if (this.user.personType === 'NATURAL') {
      const naturalPerson = <NaturalPerson> this.user;
      naturalPerson.firstname = this.masterDataForm.controls.firstname.value;
      naturalPerson.lastname = this.masterDataForm.controls.lastname.value;
      this.executeChange(naturalPerson);

    } else {
      const legalPerson = <LegalPerson> this.user;
      legalPerson.name = this.masterDataForm.controls.name.value;
      legalPerson.commercialRegisterNumber = this.masterDataForm.controls.commercialRegisterNumber.value;
      legalPerson.taxNumber = this.masterDataForm.controls.taxNumber.value;
      this.executeChange(legalPerson);

    }
  }

  executeChange(person) {
    this.masterDataService.update(person).subscribe(result => {
      this.userService.setUser(result.body);
      this.user = this.userService.getUser();
      this.toggleEditMode();
    }, error => {
      console.log(error);
    });
  }

}
