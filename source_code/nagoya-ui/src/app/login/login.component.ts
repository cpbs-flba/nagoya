import {Component, OnInit, OnDestroy} from '@angular/core';
import {Subject} from 'rxjs';
import {LoginContext} from '../model/loginContext';
import {ProgressService} from '../services/progress.service';
import {MessageService} from '../services/message.service';
import {UserService} from '../services/user.service';
import {AuthenticationService} from '../core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {first} from 'rxjs/internal/operators';
import {Router} from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-user',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  private ngUnsubscribe: Subject<any> = new Subject();
  loginRunning = false;
  error: string;
  loginForm: FormGroup;


  constructor(private userService: UserService,
              private formBuilder: FormBuilder,
              private progressService: ProgressService,
              private messageService: MessageService,
              private authenticationService: AuthenticationService,
              private router: Router,
              private toastr: ToastrService,
              private translate: TranslateService) {
    this.createForm();
  }

  ngOnInit(): void {
    if (this.isLoggedIn()) {
      this.router.navigate(['home']);
    }

  }

  ngOnDestroy(): void {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.unsubscribe();
  }

  loginUser() {
    if (this.loginForm.invalid) {
      return;
    }

    const data = new LoginContext();
    data.email = this.loginForm.controls.email.value;
    data.password = this.loginForm.controls.password.value;

    this.loginRunning = true;
    this.progressService.startWorking();
    // this.messageService.clearAll();

    this.authenticationService.login(data)
      .pipe(first())
      .subscribe(response => {
        this.router.navigate(['home']);

      }, error => {
        this.loginRunning = false;
        if (error.status === 401) {
          // this.toastr.error('Invalid Input. Please check your credentials');
          this.displayErrorMessage('LOGIN.ERROR.INVALID_INPUT');
        } else if (error.status === 403) {
          // this.toastr.error('Please confirm your E-Mail first.');
          this.displayErrorMessage('LOGIN.ERROR.NOT_CONFIRMED');
        } else {
          this.displayErrorMessage('LOGIN.ERROR.UNSPECIFIED');
        }
      });
  }


  private createForm() {
    this.loginForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  isLoggedIn() {
    return this.authenticationService.isAuthenticated();
  }

  // TODO Refactor
  displayErrorMessage(key) {
    this.translate.get(key).subscribe(value => {
      this.toastr.error(value);
    });
  }

}
