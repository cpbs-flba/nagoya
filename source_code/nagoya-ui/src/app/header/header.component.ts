import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {I18nService} from '../services/i18n.service';
import {environment} from '../../environments/environment';
import {TranslateService} from '@ngx-translate/core';
import {Router} from '@angular/router';
import {AuthenticationService} from '../core';
import {MediaMatcher} from '@angular/cdk/layout';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(public i18nService: I18nService,
              public translate: TranslateService,
              private router: Router,
              private authenticationService: AuthenticationService) {
  }

  navigationLinks = ['home', 'resource'];


  ngOnInit(): void {
    this.i18nService.init(environment.defaultLanguage, environment.supportedLanguages);
  }

  setLanguage(language: string) {
    this.i18nService.language = language;
  }

  navigateToHome() {
    this.router.navigate(['home']);
  }

  isLoggedIn() {
    return this.authenticationService.isAuthenticated();
  }
}


