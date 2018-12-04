import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, map, tap} from 'rxjs/internal/operators';
import {Observable, of} from 'rxjs';
import {User} from '../model/user';
import {environment} from '../../environments/environment';
import {LoginContext} from '../model/loginContext';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private http: HttpClient) {
  }

  public register(form, personType) {
    return this.http.put<any>(environment.serverUrl + 'users/register/' + personType, form, {
      headers: new HttpHeaders({'Content-Type': 'application/json'}),
      observe: 'response'
    });
  }

  public confirm(token) {
    return this.http.post<any>(environment.serverUrl + 'users/confirm?token=' + token, {
      headers: new HttpHeaders({'Content-Type': 'application/json'}),
      observe: 'response'
    });
  }
}
