import {Injectable} from '@angular/core';

/**
 * Service created to avoid circular dependencies
 */

@Injectable()
export class TokenService {

  private token: string;

  public getToken(): string {
    return this.token;
  }

  public setToken(token) {
    // sometimes the UI calls local services (like getting assets) and then we do not want to override the token with a null value
    if (token === undefined) {
      return;
    }
    if (token === null) {
      return;
    }
    if (token.trim() === '') {
      return;
    }
    console.log('Setting token to: ' + token);
    this.token = token;
  }

  // use this method to explicitely remove the token
  public removeToken() {
    this.token = null;
  }

}
