import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {AuthenticationService} from '../core';

@Injectable({
  providedIn: 'root'
})
export class MasterDataService {

  constructor(private http: HttpClient, private authenticationService: AuthenticationService) {
  }

  public update(person: Person) {
    return this.http.post<Person>(environment.serverUrl + 'users/update/' + person.personType.toLowerCase(), person, {
      headers: new HttpHeaders({'Content-Type': 'application/json'}),
      observe: 'response'
    });
  }
}
