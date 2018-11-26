import { ServerConfig } from '../model/serverconfig';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import * as data from '../../assets/config/serverconfig.json';
import { MessageService } from './message.service';

@Injectable({
  providedIn: 'root'
})
export class ServerConfigService {

  private config;
  private serverReachable = false;

  constructor(
    private http: HttpClient,
    private messageService: MessageService) {
    this.init();
  }

  getConfig(): ServerConfig {
    return this.config;
  }

  init() {
    this.config = data;
    if (this.config != null) {
      this.config = data;
    }
  }

  isServerReachable() {
    if (!this.isServerReachable) {
      const errorMessage = this.messageService.getI18nMessage('MESSAGES.SERVER_UNREACHABLE');
      this.messageService.error(errorMessage);
    }
    return this.isServerReachable;
  }

  pingServer() {
    const serverConfig: ServerConfig = this.config;
    if (serverConfig == null) {
      // might not be initialized yet
      return;
    }
    const requestUrl = serverConfig.serverURL + serverConfig.pathPing;

    const subs = this.http //
      .get(requestUrl, { observe: 'response' })//
      .subscribe(
        response => {
          if (response.status === 204) {
            this.serverReachable = true;
          } else {
            this.serverReachable = false;
          }
        },
        onerror => {
          this.serverReachable = false;
        }
      );
  }
}
