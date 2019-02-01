import { Component, OnInit } from '@angular/core';
import { IncompatibleBrowserService } from './services/incompatible-browser.service';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { CookiesDialogComponent } from './components/cookies-dialog/cookies-dialog.component';
import { CookieAcceptedService } from './services/cookie-accepted.service';
import { ServerConfigService } from './services/serverconfig.service';
import { ProgressService } from './services/progress.service';
import { AuthenticationService } from './core';
import { TranslateService } from '@ngx-translate/core';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  constructor(
    public translate: TranslateService,
    private incompatobleBrowserService: IncompatibleBrowserService,
    private cookieService: CookieAcceptedService,
    private serverConfigService: ServerConfigService,
    private progressService: ProgressService,
    private dialog: MatDialog,
    private authenticationService: AuthenticationService
  ) {
    this.handleCookies();
    this.incompatobleBrowserService.verifyBrowser();
    this.serverConfigService.pingServer();

    translate.addLangs(['English', 'Deutsch']);
    translate.setDefaultLang('English');

    const browserLang = translate.getBrowserLang();
    if (browserLang.startsWith('de')) {
      translate.use('Deutsch');
    } else {
      translate.use('English');
    }
  }

  ngOnInit(): void {
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

  isLoggedIn() {
    return this.authenticationService.isAuthenticated();
  }
  logout() {
    this.authenticationService.logout();
  }

}
