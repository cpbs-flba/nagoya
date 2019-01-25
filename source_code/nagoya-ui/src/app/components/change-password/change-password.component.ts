import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormGroupDirective, NgForm, Validators } from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material';
import { MasterDataService } from '../../services/master-data.service';
import { UserService } from '../../services/user.service';
import { MessageService } from 'src/app/services/message.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {

  pwChangeForm: FormGroup;
  matcher = new MyErrorStateMatcher();

  constructor(private formBuilder: FormBuilder,
    private masterDataService: MasterDataService,
    private userService: UserService,
    private messageService: MessageService) {
    this.pwChangeForm = this.createForm();
  }

  ngOnInit() {
  }

  createForm(): FormGroup {
    return this.formBuilder.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      newPasswordConfirmation: ['', Validators.required]
    }, { validator: this.checkPasswords });
  }

  onSubmit() {
    const person = this.userService.getUser();
    
    person.passwordConfirmation = this.pwChangeForm.controls.oldPassword.value;
    person.password = this.pwChangeForm.controls.newPassword.value;

    this.masterDataService.update(person).subscribe(result => {
      this.userService.setUser(result.body);
      // reset the form content
      this.pwChangeForm = this.createForm();
      // send OK message
      this.messageService.displayInfoMessage('CHANGE_PASSWORD.INFO.PW_CHANGE_OK');
    }, error => {
      if (error.status == 403) {
        this.messageService.displayErrorMessage('CHANGE_PASSWORD.ERROR.OLD_PW_NOK');
      }
    });
  }

  checkPasswords(group: FormGroup) {
    let newPassword = group.controls.newPassword.value;
    let newPasswordConfirmation = group.controls.newPasswordConfirmation.value;
    if (newPassword == null || newPasswordConfirmation == null) {
      return true;
    }
    return newPassword === newPasswordConfirmation ? null : { notSame: true };
  }
}

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const invalidCtrl = !!(control && control.invalid && control.parent.dirty);
    const invalidParent = !!(control && control.parent && control.parent.invalid && control.parent.dirty);
    return (invalidCtrl || invalidParent);
  }
}
