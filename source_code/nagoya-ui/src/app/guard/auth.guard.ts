import {CanActivate, Router} from '@angular/router';

export class AuthGuard implements CanActivate{
  constructor(
    private router: Router,
    // private authenticationService:
  ) {}
}
