import {Component, Input, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-resource',
  templateUrl: './resource.component.html',
  styleUrls: ['./resource.component.scss']
})
export class ResourceComponent implements OnInit {

  createNew = false;
  reloadChanges = false;

  constructor(public translate: TranslateService) {
  }

  ngOnInit() {

  }

  toggleResourceCreation() {
    this.createNew = !this.createNew;
  }

  reloadResources() {
    this.reloadChanges = true;
  }

}
