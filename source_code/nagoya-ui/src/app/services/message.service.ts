import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import {ToastrService} from 'ngx-toastr';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  errorMessages: string[] = [];
  infoMessages: string[] = [];
  successMessages: string[] = [];
  warningMessages: string[] = [];

  constructor(
    private translate: TranslateService,
    private toastr: ToastrService,
  ) {
    // noop
  }

  getI18nMessage(messageId: string): string {
    return this.translate.instant(messageId);
  }

  clearAll() {
    this.errorMessages = [];
    this.warningMessages = [];
    this.infoMessages = [];
    this.successMessages = [];
  }

  clearError() {
    this.errorMessages = [];
  }
  error(message: string) {
    this.errorMessages.push(message);
  }

  clearWarning() {
    this.warningMessages = [];
  }
  warning(message: string) {
    this.warningMessages.push(message);
  }

  clearInfo() {
    this.infoMessages = [];
  }
  info(message: string) {
    this.infoMessages.push(message);
  }

  clearSuccess() {
    this.successMessages = [];
  }
  success(message: string) {
    this.successMessages.push(message);
  }

  displayErrorMessage(key) {
    this.translate.get(key).subscribe(value => {
      this.toastr.error(value);
    });
  }

  displayInfoMessage(key) {
    this.translate.get(key).subscribe(value => {
      this.toastr.info(value);
    });
  }
}
