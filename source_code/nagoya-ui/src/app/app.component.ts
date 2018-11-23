import { Component } from '@angular/core';
import { IncompatibleBrowserService } from './incompatible-browser.service';
import { TranslateService } from '@ngx-translate/core';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { CookiesDialogComponent } from './cookies-dialog/cookies-dialog.component';
import { CookieAcceptedService } from './cookie-accepted.service';
import { UserService } from './user.service';
import { ServerConfigService } from './serverconfig.service';
import { MessageService } from './message.service';
import { ProgressService } from './progress.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Nagoya UI';

  constructor(
    public incompatobleBrowserService: IncompatibleBrowserService,
    public translate: TranslateService,
    public cookieService: CookieAcceptedService,
    public messageService: MessageService,
    private userService: UserService,
    private serverConfigService: ServerConfigService,
    private progressService: ProgressService,
    private dialog: MatDialog
  ) {
    this.handleLanguages();
    this.handleCookies();
    incompatobleBrowserService.verifyBrowser();
    this.serverConfigService.pingServer();
  }

  handleLanguages() {
    this.translate.addLangs(['English', 'Deutsch']);
    this.translate.setDefaultLang('English');

    const browserLang = this.translate.getBrowserLang();
    if (browserLang.startsWith('de')) {
      this.translate.use('Deutsch');
    } else {
      this.translate.use('English');
    }
  }

  isLoggedIn() {
    return this.userService.isLoggedIn();
  }

  handleCookies() {
    var cookiesAllowed = this.cookieService.areCookiesAllowed();
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
