import { Component, OnInit } from '@angular/core';
import {I18nService} from '../services/i18n.service';
import {environment} from '../../environments/environment';
import {TranslateService} from '@ngx-translate/core';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private i18nService: I18nService,
              private translate: TranslateService) {

  }


  ngOnInit(): void {
    this.i18nService.init(environment.defaultLanguage, environment.supportedLanguages);
  }

  setLanguage(language: string){
    console.log(language);
    this.i18nService.language = language;
  }
}


