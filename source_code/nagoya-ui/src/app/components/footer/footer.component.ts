import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { AuthenticationService } from '../../core';
import { Router } from '@angular/router';
import { MessageService } from 'src/app/services/message.service';
import { UserService } from 'src/app/services/user.service';
import { TokenService } from 'src/app/core/authentication/token.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  selectedLanguage: string;

  constructor(public translate: TranslateService,
    private authenticationService: AuthenticationService,
   
    private messageService: MessageService,
    private router: Router) {
  }

  ngOnInit(): void {
    this.selectedLanguage = this.translate.currentLang;
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
