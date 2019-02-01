import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { HttpResponse, HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class BlockchainExplorerService {

  private assets: [];

  constructor(private http: HttpClient) { }

  public search(query): Observable<any> {
    let requestUrl = environment.serverUrl + 'search?query=' + query;
    return this.http.get<HttpResponse<any>>(requestUrl, { observe: 'response' })
      .pipe(
        tap(result => {
          this.assets = result.body;
        }));
  }

  public getAssets() {
    return this.assets;
  }

}
