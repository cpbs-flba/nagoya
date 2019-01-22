import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Contract } from '../model/contract/contract';

import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { TokenService } from '../core/authentication/token.service';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ContractsService {

  contracts;

  constructor(private http: HttpClient,
    public tokenService: TokenService) {
  }

  public getAll(dateFrom, dateUntil, status): Observable<any> {
    let requestUrl = environment.serverUrl + 'contracts?';
    requestUrl += 'status=' + status + '&';
    requestUrl += 'date-from=' + dateFrom + '&';
    requestUrl += 'date-until=' + dateUntil;
    return this.http.get(requestUrl, {
      headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': this.tokenService.getToken() }),
      observe: 'response'
    }).pipe(
      tap(result => {
        // verify status
        this.tokenService.setToken(result.headers.get('Authorization'));
        this.contracts = result.body;
      }));
  }

  getContracts(): Contract[] {
    return this.contracts;
  }
}
