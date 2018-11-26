import { Injectable } from '@angular/core';
import { User } from '../model/user';
import { CookieService } from 'ngx-cookie';
import { Observable } from 'rxjs';
import { ServerConfig } from '../model/serverconfig';
import { ServerConfigService } from './serverconfig.service';
import { MessageService } from './message.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  static user: User;
  static loggedIn = false;

  private SKEY = 'nagoya_session_skey';
  private SUSR = 'nagoya_session_user';

  private token;

  constructor(
    private serverConfigService: ServerConfigService,
    private http: HttpClient,
    private messageService: MessageService,
    private cookieService: CookieService) {
    // noop
  }

  isLoggedIn(): boolean {
    this.reloadFromCookies();
    return UserService.loggedIn;
  }

  login(user): Observable<User> {
    const serverConfig: ServerConfig = this.serverConfigService.getConfig();
    let requestUrl;
    // do we have to even try?
    if (!this.serverConfigService.isServerReachable) {
      return null;
    }
  }

  reloadFromCookies() {
    // do we have a session cookie?
    const currentSessionKey = this.cookieService.get(this.SKEY);
    if (currentSessionKey == null) {
      return;
    }

    // we have a session cookie
    // is it expired?
    const fields = currentSessionKey.split('.');
    const cookieContent = atob(fields[1]);
    const cookieContentObject = JSON.parse(cookieContent);
    const deadline = cookieContentObject.deadline;
    const currentDate = new Date().getTime();
    const delta = deadline - currentDate;
    if (delta < 0) {
      // session expired
      // console.log('Session expired.')
      this.clearCookies();
      return;
    }

    const currentUser = this.cookieService.get(this.SUSR);
    if (currentUser == null) {
      return;
    }

    if (UserService.user == null) {
      this.setUser(JSON.parse(currentUser));
      UserService.loggedIn = true;
      this.setToken(currentSessionKey);
    }
  }

  setToken(token) {
    const sessionKey = this.SKEY;
    this.cookieService.put(sessionKey, token);
    this.token = token;
  }

  getUser() {
    return UserService.user;
  }

  setUser(value: User) {
    UserService.user = value;
    if (value != null) {
      this.cookieService.put(this.SUSR, JSON.stringify(value));
    }
  }

  clearCookies() {
    this.cookieService.remove(this.SKEY);
    this.cookieService.remove(this.SUSR);
  }

  logout(): void {
    // do we have to even try?
    if (!this.serverConfigService.isServerReachable) {
      return;
    }

    const serverConfig: ServerConfig = this.serverConfigService.getConfig();
    const requestUrl = serverConfig.serverURL + serverConfig.pathLogout;
    this.http.get(requestUrl, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': this.token }),
      observe: 'response'
    }).subscribe(
      onsuccess => {
        this.clearCookies();
        UserService.loggedIn = false;
        UserService.user = null;
      }
    );
  }
}
