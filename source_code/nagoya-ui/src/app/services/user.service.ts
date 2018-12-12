import {Injectable} from '@angular/core';
import {User} from '../model/user';
import {CookieService} from 'ngx-cookie';
import {Observable} from 'rxjs';
import {ServerConfig} from '../model/serverconfig';
import {ServerConfigService} from './serverconfig.service';
import {MessageService} from './message.service';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private person: Person;

  constructor(private serverConfigService: ServerConfigService,
              private http: HttpClient,
              private messageService: MessageService,
              private cookieService: CookieService) {
  }

  setUser(userData) {
    this.person = userData;
  }

  getUser(): Person {
    return this.person;
  }


}
