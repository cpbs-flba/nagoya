import { Component, OnInit } from '@angular/core';
import {environment} from '../../environments/environment';
import {I18nService} from '../services/i18n.service';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit {

  constructor(private i18nService: I18nService, private translate: TranslateService) { }


  ngOnInit(): void {
    this.i18nService.init(environment.defaultLanguage, environment.supportedLanguages);
  }

  setLanguage(language: string){
    console.log(language);
    this.i18nService.language = language;
  }

}
