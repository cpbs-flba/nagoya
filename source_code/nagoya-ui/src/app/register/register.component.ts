import { Component, OnInit, OnDestroy } from '@angular/core';
import { User } from '../model/user';
import { UserService } from '../user.service';
import { ProgressService } from '../progress.service';
import { MessageService } from '../message.service';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit, OnDestroy {

  private ngUnsubscribe: Subject<any> = new Subject();

  constructor(private userService: UserService, public progressService: ProgressService, public messageService: MessageService) { }

  user: User = {
    email: '',
    password: '',
    firstname: '',
    lastname: ''
  };

  ngOnInit() {
  }

  ngOnDestroy(): void {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.unsubscribe();
  }

  registerUser() {
    this.progressService.startWorking();
    this.messageService.clearAll();
  }

  isLoggedIn() {
    return this.userService.isLoggedIn();
  }


}
