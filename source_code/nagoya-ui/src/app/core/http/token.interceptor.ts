import {Injectable} from '@angular/core';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {TokenService} from '../authentication/token.service';
import {tap} from 'rxjs/internal/operators';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  constructor(public tokenService: TokenService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token = this.tokenService.getToken();

    if (token) {
      request = request.clone({
        headers: request.headers.set('Authorization', token)
      });
    }
    return next.handle(request).pipe(tap((event: HttpEvent<any>) => {
      if (event instanceof HttpResponse) {
        token = event.headers.get('Authorization');
        this.tokenService.setToken(token);
      }
      return event;
    }));

  }
}
