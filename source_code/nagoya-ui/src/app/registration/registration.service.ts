import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, tap} from 'rxjs/internal/operators';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private http: HttpClient) {
  }

  public register(form) {
    this.http.put<any>('http://localhost:13200/rest/users/register/legal', form, {
      headers: new HttpHeaders({'Content-Type': 'application/json'}),
      observe: 'response'
    })
      .pipe(
      tap(result => {
        console.log(result);
      }),
      catchError(this.handleError<any>('login'))
    )
      .subscribe();
  }

  private handleError<T>(operation = 'operation', result ?: T) {
    return (error: any): Observable<T> => {

      console.error(error); // log to console instead

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

}
