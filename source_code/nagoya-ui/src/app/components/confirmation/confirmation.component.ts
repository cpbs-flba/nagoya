import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RegistrationService } from '../../services/registration.service';
import { MessageService } from '../../services/message.service';

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.scss']
})
export class ConfirmationComponent implements OnInit {

  afterRegistration: boolean;
  startedConfirmation: boolean = false;
  tokenConfirmed: boolean;
  spinner = false;
  token: string;
  personResponse: Person;

  constructor(private route: ActivatedRoute,
    private registrationService: RegistrationService,
    private messageService: MessageService) {
  }

  ngOnInit() {
    this.tokenConfirmed = false;
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
    });
    if (this.token && this.token.trim().length > 0) {
      this.afterRegistration = false;
      this.confirmToken();
    } else {
      this.afterRegistration = true;
    }
  }

  confirmToken() {
    this.startedConfirmation = true;
    this.registrationService.confirm(this.token)
      .subscribe(response => {
        this.startedConfirmation = false;
        this.tokenConfirmed = true;
        this.personResponse = response.body;
      }, error => {
          this.startedConfirmation = false;

          if (error.status === 400) {
            this.messageService.displayErrorMessage('CONFIRMATION.ERROR.TOKEN_INVALID');
          } else if (error.status === 408) {
            this.messageService.displayErrorMessage('CONFIRMATION.ERROR.TOKEN_EXPIRED');
          } else {
            this.messageService.displayErrorMessage('CONFIRMATION.ERROR.REGISTRATION_FAILED');
          }
        });
  }

}
