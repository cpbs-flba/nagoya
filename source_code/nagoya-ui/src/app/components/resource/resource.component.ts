import {Component, Input, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-resource',
  templateUrl: './resource.component.html',
  styleUrls: ['./resource.component.scss']
})
export class ResourceComponent implements OnInit {

  createNew = false;
  reloadChanges = false;
  loggedIn = false;

  constructor(public translate: TranslateService, public userService : UserService) {
  }

  ngOnInit() {
    if (this.userService.getUser() !== undefined && this.userService.getUser() !== null) {
      this.loggedIn = true;
    }
  }

  toggleResourceCreation() {
    this.createNew = !this.createNew;
  }

  reloadResources() {
    this.reloadChanges = true;
  }

}
