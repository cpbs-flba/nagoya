import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap, Router} from '@angular/router';
import {switchMap} from 'rxjs/internal/operators';
import {RegistrationService} from '../services/registration.service';
import {ToastrService} from 'ngx-toastr';

@Component({
  selector: 'app-confirmation',
  templateUrl: './confirmation.component.html',
  styleUrls: ['./confirmation.component.scss']
})
export class ConfirmationComponent implements OnInit {

  afterRegistration: boolean;
  startedConfirmation: boolean = false;
  tokenConfirmed: boolean;
  spinner: boolean = false;
  token: string;

  constructor(private route: ActivatedRoute,
              private registrationService: RegistrationService,
              private router: Router,
              private toastr: ToastrService) {

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
        // this.router.navigate(['confirmation']);
        console.log(response);
        this.handleSuccess();
      }, error => {
        this.startedConfirmation = false;
        if (error.status === 400) {
          this.toastr.error('Your token is invalid');
        } else if (error.status === 408) {
          this.toastr.error('Your token has expired');
          // TODO decide what to do
        } else {
          this.toastr.error('Registration failed');
        }
      });
  }

  handleSuccess() {
    this.delay(3000);

  }

  async delay(ms: number) {
    await new Promise(resolve => setTimeout(() => resolve(), ms)).then(() => this.startedConfirmation = false);
  }


}
