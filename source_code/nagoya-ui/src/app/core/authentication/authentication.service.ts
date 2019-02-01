import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { map, tap } from 'rxjs/internal/operators';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { ServerConfigService } from '../../services/serverconfig.service';
import { LoginContext } from '../../model/loginContext';
import { User } from '../../model/user';
import { TokenService } from './token.service';

export interface Credentials {
  // Customize received credentials here
  email: string;
  token: string;
}


const credentialsKey = 'credentials';

/**
 * Provides a base for authentication workflow.
 * The Credentials interface as well as login/logout methods should be replaced with proper implementation.
 */
@Injectable()
export class AuthenticationService {
  private _credentials: Credentials | null;

  constructor(private http: HttpClient, private serverConfigService: ServerConfigService, private tokenServie: TokenService) {
    const savedCredentials = sessionStorage.getItem(credentialsKey);
    if (savedCredentials) {
      this._credentials = JSON.parse(savedCredentials);
    }
  }

  /**
   * Authenticates the user.
   * @param context The login parameters.
   * @return The user credentials.
   */
  login(context: LoginContext): Observable<any> {
    return this.http.post<HttpResponse<User>>(environment.serverUrl + 'users/login', context, { observe: 'response' })
      .pipe(
        tap(response => {
          const data = {
            email: response.body.email,
            token: response.headers.get('Authorization')
          };
          this.setCredentials(data);
          this.tokenServie.setToken(response.headers.get('Authorization'))
          return response.body;
        }));

  }

  /**
   * Logs out the user and clear credentials.
   * @return True if the user was logged out successfully.
   */
  logout(): Observable<any> {
    this.setCredentials();
    return this.http.get<HttpResponse<any>>(environment.serverUrl + 'users/logout', { observe: 'response' })
      .pipe(
        tap(response => {
          // noop
        }));
  }

  /**
   * Checks is the user is authenticated.
   * @return True if the user is authenticated.
   */
  isAuthenticated(): boolean {
    return !!this.credentials;
  }

  /**
   * Gets the user credentials.
   * @return The user credentials or null if the user is not authenticated.
   */
  get credentials(): Credentials | null {
    return this._credentials;
  }

  /**
   * Sets the user credentials.
   * The credentials may be persisted across sessions by setting the `remember` parameter to true.
   * Otherwise, the credentials are only persisted for the current session.
   * @param credentials The user credentials.
   * @param remember True to remember credentials across sessions.
   */
  private setCredentials(credentials?: Credentials) {
    this._credentials = credentials || null;

    if (credentials) {
      const storage = sessionStorage;
      storage.setItem(credentialsKey, JSON.stringify(credentials));
    } else {
      sessionStorage.removeItem(credentialsKey);
    }
  }

  getCredentials() {

  }
}
