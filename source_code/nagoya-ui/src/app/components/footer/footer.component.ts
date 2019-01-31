import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { AuthenticationService } from '../../core';
import { Router } from '@angular/router';
import { MessageService } from 'src/app/services/message.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  constructor(public translate: TranslateService,
    private authenticationService: AuthenticationService,
    private messageService: MessageService,
    private router: Router) {
  }


  ngOnInit(): void {
  }

  isLoggedIn() {
    return this.authenticationService.isAuthenticated();
  }

  logout() {
    // logout REST call
    this.authenticationService.logout()
      .subscribe(response => {
        this.messageService.displayInfoMessage('FOOTER.LOGOUT_OK');
        this.router.navigate(['login']);
      }, error => {
        this.messageService.displayInfoMessage('FOOTER.LOGOUT_OK');
        this.router.navigate(['login']);
      });
  }


}
