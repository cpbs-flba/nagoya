import { Component } from '@angular/core';
import { IncompatibleBrowserService } from './incompatible-browser.service';
import { TranslateService } from '@ngx-translate/core';

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
  ) {
    translate.addLangs(['English', 'Deutsch']);
    translate.setDefaultLang('English');

    const browserLang = translate.getBrowserLang();
    if (browserLang.startsWith('de')) {
      translate.use('Deutsch');
    } else {
      translate.use('English');
    }

    incompatobleBrowserService.verifyBrowser();
  }

}
