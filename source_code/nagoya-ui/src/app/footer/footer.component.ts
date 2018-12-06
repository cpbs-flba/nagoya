import {Component, OnInit} from '@angular/core';
import {environment} from '../../environments/environment';
import {I18nService} from '../services/i18n.service';
import {TranslateService} from '@ngx-translate/core';
import {AuthenticationService} from '../core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  constructor(public i18nService: I18nService,
              public translate: TranslateService,
              private authenticationService: AuthenticationService,
              private router: Router) {
  }


  ngOnInit(): void {
    this.i18nService.init(environment.defaultLanguage, environment.supportedLanguages);
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  isLoggedIn() {
    return this.authenticationService.isAuthenticated();
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['login']);
  }


}
