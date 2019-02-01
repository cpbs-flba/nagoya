import {Component, Input, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {GeneticResource} from '../../../model/geneticResource';
import {ResourceService} from '../../../services/resource.service';
import {GeneticResourceFilter} from '../../../model/geneticResourceFilter';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit, OnDestroy {

  geneticResourceFilter = new GeneticResourceFilter();
  geneticResources: GeneticResource[];

  @Input() reloadChanges: boolean;

  ngOnChanges(changes: SimpleChanges) {
    if (changes.reloadChanges.currentValue) {
      this.getAllResources();
    }
  }

  constructor(private resourceService: ResourceService) {
  }

  ngOnInit() {
    this.getAllResources();
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

  getAllResources() {
    this.resourceService.getAll().subscribe(response => {
      this.geneticResources = response;
    }, error => {
      //TODO
      console.log(error);
    });
  }


}
