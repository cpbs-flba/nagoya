import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';
import { User } from '../model/user';
import { ProgressService } from '../services/progress.service';
import { MessageService } from '../services/message.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit, OnDestroy {

  private ngUnsubscribe: Subject<any> = new Subject();

  loginRunning = false;

  passwordConfirmation;

  user: User = {
    email: '',
    password: '',
    firstname: '',
    lastname: ''
  };

  constructor(private userService: UserService, public progressService: ProgressService, public messageService: MessageService) {
    // noop
   }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.unsubscribe();
  }

  loginUser() {
    this.loginRunning = true;
    this.progressService.startWorking();
    this.messageService.clearAll();
  }

  isLoggedIn() {
    return this.userService.isLoggedIn();
  }
}
