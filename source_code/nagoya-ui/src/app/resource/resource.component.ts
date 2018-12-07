import {Component, OnInit} from '@angular/core';

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

  createNewResource() {
    this.createNew = !this.createNew;
  }

}
