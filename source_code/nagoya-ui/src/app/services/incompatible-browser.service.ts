import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class IncompatibleBrowserService {

  constructor(private router: Router) { }

  verifyBrowser() {
    let browserOK = false;

    const firefoxVersion = this.getBrowserVersion('Firefox');
    if (firefoxVersion != null && firefoxVersion > 60) {
      browserOK = true;
      // console.log("Using Firefox: " + firefoxVersion);
    }

    const chromeVersion = this.getBrowserVersion('Chrome');
    if (chromeVersion != null && chromeVersion > 60) {
      browserOK = true;
      // console.log("Using Chrome: " + chromeVersion);
    }

    const edgeVersion = this.getBrowserVersion('Edge');
    if (edgeVersion != null && edgeVersion > 16) {
      browserOK = true;
      // console.log("Using Edge: " + edgeVersion);
    }

    const safariVersion = this.getBrowserVersion('Safari');
    if (safariVersion != null && safariVersion > 500) {
      browserOK = true;
      // console.log("Using Safari: " + safariVersion);
    }

    const appleKit = this.getBrowserVersion('AppleWebKit');
    if (appleKit != null && appleKit > 500) {
      browserOK = true;
      // console.log("Using appleKit: " + appleKit);
    }

    const operaVersion = this.getBrowserVersion('OPR');
    if (operaVersion != null && operaVersion > 50) {
      browserOK = true;
      // console.log("Using Opera: " + operaVersion);
    }
    // console.log('Browser is ok: ' + browserOK);

    if (!browserOK) {
      this.router.navigate(['browsernotok']);
    }
  }

  getBrowserVersion(browserName): any {
    const userAgent = window.navigator.userAgent;
    const browserStart = userAgent.indexOf(browserName);
    if (browserStart > 0) {
      const fVersion = userAgent.substr(browserStart + browserName.length + 1, userAgent.length);
      const indexUntil = fVersion.indexOf('.');
      const versionString = fVersion.substr(0, indexUntil);
      const version = Number(versionString);
      return version;
    } else {
      return null;
    }
  }
}
