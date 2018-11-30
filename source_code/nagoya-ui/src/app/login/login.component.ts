import {Component, OnInit, OnDestroy} from '@angular/core';
import {Subject} from 'rxjs';
import {LoginContext} from '../model/loginContext';
import {ProgressService} from '../services/progress.service';
import {MessageService} from '../services/message.service';
import {UserService} from '../services/user.service';
import {AuthenticationService} from '../core';
import {Input} from '@angular/compiler/src/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {first} from 'rxjs/internal/operators';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {

  private ngUnsubscribe: Subject<any> = new Subject();
  // @Input() loginContext: LoginContext;
  loginRunning = false;
  error: string;
  loginForm: FormGroup;


  constructor(private userService: UserService,
              private formBuilder: FormBuilder,
              private progressService: ProgressService,
              private messageService: MessageService,
              private authenticationService: AuthenticationService,
              private router: Router) {
    this.createForm();
  }

  ngOnInit(): void {

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
    this.messageService.clearAll();

    this.authenticationService.login(data)
      .pipe(first())
      .subscribe(response => {
        this.router.navigate(['terms']);

      }, error => {
        console.log('error, invalid data');
      });
  }

  isLoggedIn() {
    return this.userService.isLoggedIn();
  }

  private createForm() {
    this.loginForm = this.formBuilder.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });
  }
}
