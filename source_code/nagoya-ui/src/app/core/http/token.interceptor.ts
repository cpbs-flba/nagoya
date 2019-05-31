import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenService } from '../authentication/token.service';
import { tap } from 'rxjs/internal/operators';
import { MessageService } from 'src/app/services/message.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  constructor(public tokenService: TokenService,
    private messageService: MessageService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let token = this.tokenService.getToken();
    if (token) {
      console.log('Requesting with token: ' + token);
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
    },
      error => {
        const errorObject = error.error;
        if (errorObject !== undefined && errorObject !== null) {
          const e1 = errorObject[0];
          if (e1 !== undefined && e1 !== null) {
            const e1Code = e1.errorCode;
            if (e1Code !== undefined && e1Code !== null) {
              const messageCode = 'SERVER.' + e1Code;
              this.messageService.displayErrorMessage(messageCode);
            }
          }
        }
      }

    ));

  }
}
