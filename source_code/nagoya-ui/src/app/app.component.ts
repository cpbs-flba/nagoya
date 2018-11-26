import {Component, OnInit} from '@angular/core';
import { IncompatibleBrowserService } from './services/incompatible-browser.service';
import { TranslateService } from '@ngx-translate/core';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { CookiesDialogComponent } from './cookies-dialog/cookies-dialog.component';
import { CookieAcceptedService } from './services/cookie-accepted.service';
import { UserService } from './services/user.service';
import { ServerConfigService } from './services/serverconfig.service';
import { MessageService } from './services/message.service';
import { ProgressService } from './services/progress.service';
import {I18nService} from './services/i18n.service';
import {environment} from '../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'Nagoya UI';

  constructor(
    public incompatobleBrowserService: IncompatibleBrowserService,
    public translate: TranslateService,
    public cookieService: CookieAcceptedService,
    public messageService: MessageService,
    private i18nService: I18nService,
    private userService: UserService,
    private serverConfigService: ServerConfigService,
    private progressService: ProgressService,
    private dialog: MatDialog
  ) {
    // this.handleLanguages();
    this.handleCookies();
    incompatobleBrowserService.verifyBrowser();
    this.serverConfigService.pingServer();
  }

  ngOnInit(): void {
    this.i18nService.init(environment.defaultLanguage, environment.supportedLanguages);

  }

  setLanguage(language: string) {
    console.log(language);
    this.i18nService.language = language;
  }

  isLoggedIn() {
    return this.userService.isLoggedIn();
  }

  handleCookies() {
    const cookiesAllowed = this.cookieService.areCookiesAllowed();
    if (!cookiesAllowed) {
      const dialogConfig = new MatDialogConfig();
      dialogConfig.disableClose = true;
      dialogConfig.autoFocus = true;
      this.dialog.open(CookiesDialogComponent, dialogConfig);
    }
  }

  showSpinner() {
    if (this.progressService.getWorking()) {
      return true;
    }
    return false;
  }

}
