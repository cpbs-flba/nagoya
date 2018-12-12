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
    this.token = token;
  }

}
