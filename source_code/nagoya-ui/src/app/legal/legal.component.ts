import { Component, OnInit } from '@angular/core';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-legal',
  templateUrl: './legal.component.html',
  styleUrls: ['./legal.component.css']
})
export class LegalComponent implements OnInit {

  constructor(public translate: TranslateService) { }

  ngOnInit() {
  }

}
