import { Component } from '@angular/core';
import { IncompatibleBrowserService } from './incompatible-browser.service';
import { TranslateService } from '@ngx-translate/core';
import { MatDialog, MatDialogConfig } from '@angular/material';
import { CookiesDialogComponent } from './cookies-dialog/cookies-dialog.component';
import { CookieAcceptedService } from './cookie-accepted.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'nagoya-ui';


  constructor(
    public incompatobleBrowserService: IncompatibleBrowserService,
    public translate: TranslateService,
    public cookieService: CookieAcceptedService,
    private dialog: MatDialog
  ) {
    this.handleLanguages();
    this.handleCookies();
    incompatobleBrowserService.verifyBrowser();
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

  handleCookies() {
    var cookiesAllowed = this.cookieService.areCookiesAllowed();
    if (!cookiesAllowed) {
      const dialogConfig = new MatDialogConfig();
      dialogConfig.disableClose = true;
      dialogConfig.autoFocus = true;
      this.dialog.open(CookiesDialogComponent, dialogConfig);
    }
  }

}
