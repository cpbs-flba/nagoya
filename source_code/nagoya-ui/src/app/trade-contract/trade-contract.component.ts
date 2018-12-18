import { Component, OnInit } from '@angular/core';
import {ResourceFile} from '../model/resourceFile';
import {ResourceService} from '../services/resource.service';
import {GeneticResource} from '../model/geneticResource';

@Component({
  selector: 'app-trade-contract',
  templateUrl: './trade-contract.component.html',
  styleUrls: ['./trade-contract.component.scss']
})
export class TradeContractComponent implements OnInit {

  geneticResources: GeneticResource [];
  attachments: ResourceFile[] = [];


  constructor(private resourceService: ResourceService) { }

  ngOnInit() {
    this.resourceService.getAll().subscribe(response => {
      this.geneticResources = response;
    }, error => {
      //TODO
      console.log(error);
    });
  }

  addAttachment(event: any) {
    const reader = new FileReader();
    const resourceFile = new ResourceFile();
    if (event.target.files && event.target.files.length) {
      const [file] = event.target.files;
      resourceFile.name = file.name;
      resourceFile.type = file.type;
      reader.readAsDataURL(file);

      reader.onload = () => {
        resourceFile.content = reader.result.toString().split(',')[1];
        this.attachments.push(resourceFile);
      };
    }
  }

  removeAttachment(resourceFile) {
    const index = this.attachments.indexOf(resourceFile, 0);
    if (index > -1) {
      this.attachments.splice(index, 1);
    }
  }

}
