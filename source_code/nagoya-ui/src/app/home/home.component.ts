import {Component, OnInit} from '@angular/core';
import {AuthenticationService, Credentials} from '../core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  userCredentials: Credentials;
  email: string;

  constructor(private authenticationService: AuthenticationService) {
  }

  ngOnInit() {
    this.userCredentials = this.authenticationService.credentials;
    this.email = this.userCredentials.email;
  }


}
