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

  public getAll(dateFrom, dateUntil, status, role): Observable<any> {
    let requestUrl = environment.serverUrl + 'contracts?';
    requestUrl += 'status=' + status + '&';
    requestUrl += 'date-from=' + dateFrom + '&';
    requestUrl += 'date-until=' + dateUntil;
    requestUrl += 'role=' + role;
    return this.http.get(requestUrl, {
      observe: 'response'
    }).pipe(
      tap(result => {
        this.contracts = result.body;
      }));
  }

  public cancel(contractId): Observable<any> {
    const requestUrl = environment.serverUrl + 'contracts/' + contractId;
    return this.http.delete(requestUrl, {
      observe: 'response'
    }).pipe(
      tap(result => {
        // verify status
        // console.log('Received: ' + result.status);
      }));
  }

  public reject(token): Observable<any> {
    const requestUrl = environment.serverUrl + 'contracts/reject/' + token;
    return this.http.get(requestUrl, {
      observe: 'response'
    }).pipe(
      tap(result => {
        // noop
      }));
  }

  public accept(token, privateKey): Observable<any> {
    let requestUrl = environment.serverUrl + 'contracts/accept/' + token;
    if (privateKey != null) {
      requestUrl += "?privatekey=" + privateKey;
    }
    return this.http.get(requestUrl, {
      observe: 'response'
    }).pipe(
      tap(result => {
        // noop
      }));
  }



  getContracts(): Contract[] {
    return this.contracts;
  }
}
