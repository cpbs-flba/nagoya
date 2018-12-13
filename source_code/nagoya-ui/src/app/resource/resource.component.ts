import {Component, OnInit} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Component({
  selector: 'app-resource',
  templateUrl: './resource.component.html',
  styleUrls: ['./resource.component.scss']
})
export class ResourceComponent implements OnInit {

  createNew = false;

  constructor() {
  }

  ngOnInit() {

  }

  toggleResourceCreation() {
    this.createNew = !this.createNew;
  }

}
