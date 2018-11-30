import {Injectable} from '@angular/core';
import {CookieService} from 'ngx-cookie';

@Injectable({
  providedIn: 'root'
})
export class CookieAcceptedService {

  private COOKIES_ACCEPTED_NAME = 'nagoya_cookies_accepted';

  constructor(private cookieService: CookieService) {
    // empty
  }

  cookiesAccepted() {
    const val = 'true';
    this.cookieService.put(this.COOKIES_ACCEPTED_NAME, val);
  }

  areCookiesAllowed() {
    const cookiesAccepted = this.cookieService.get(this.COOKIES_ACCEPTED_NAME);

    if (cookiesAccepted && cookiesAccepted.trim().toLowerCase() === 'true') {
      return true;
    }
    return false;
  }

}
