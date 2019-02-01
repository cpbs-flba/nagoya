import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private person: Person;

  constructor() {
  }

  setUser(userData) {
    this.person = userData;
  }

  getUser(): Person {
    return this.person;
  }

}
