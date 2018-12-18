import {Component, OnDestroy, OnInit} from '@angular/core';
import {GeneticResource} from '../../model/geneticResource';
import {ResourceService} from '../../services/resource.service';
import {GeneticResourceFilter} from '../../model/geneticResourceFilter';

@Component({
  selector: 'app-template',
  templateUrl: './template.component.html',
  styleUrls: ['./template.component.scss']
})
export class TemplateComponent implements OnInit, OnDestroy {

  geneticResourceFilter = new GeneticResourceFilter();
  geneticResources: GeneticResource[];

  constructor(private resourceService: ResourceService) {
  }

  ngOnInit() {
    this.resourceService.getAll().subscribe(response => {
      this.geneticResources = response;
    }, error => {
      //TODO
      console.log(error);
    });

  }
  ngOnDestroy(): void {
  }

  search() {
    this.resourceService.search(this.geneticResourceFilter).subscribe(response => {
      this.geneticResources = response;
      console.log('received response');
    }, error => {
      //TODO
      console.log(error);
    });
  }



}
