import {Component, OnInit} from '@angular/core';
import {Form, FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material';
import {MasterDataService} from '../services/master-data.service';
import {UserService} from '../services/user.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  pwChangeForm: FormGroup;
  matcher = new MyErrorStateMatcher();


  constructor(private formBuilder: FormBuilder, private masterDataService: MasterDataService, private userService: UserService) {
    this.pwChangeForm = this.createForm();
  }

  ngOnInit() {
  }

  createForm(): FormGroup {
    return this.formBuilder.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      newPasswordConfirmation: ['', Validators.required]
    }, {validator: this.checkPasswords});
  }

  onSubmit() {
    const person = this.userService.getUser();
    person.passwordConfirmation = this.pwChangeForm.controls.oldPassword.value;
    person.password = this.pwChangeForm.controls.newPassword.value;

    this.masterDataService.update(person).subscribe(result => {
      this.userService.setUser(result.body);
    }, error => {
      console.log(error);
    });
  }

  checkPasswords(group: FormGroup) {
    let newPassword = group.controls.newPassword.value;
    let newPasswordConfirmation = group.controls.newPasswordConfirmation.value;

    return newPassword === newPasswordConfirmation ? null : {notSame: true};
  }
}

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const invalidCtrl = !!(control && control.invalid && control.parent.dirty);
    const invalidParent = !!(control && control.parent && control.parent.invalid && control.parent.dirty);

    return (invalidCtrl || invalidParent);
  }
}
