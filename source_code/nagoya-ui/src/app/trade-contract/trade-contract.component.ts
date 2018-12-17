import { Component, OnInit } from '@angular/core';
import {ResourceFile} from '../model/resourceFile';

@Component({
  selector: 'app-trade-contract',
  templateUrl: './trade-contract.component.html',
  styleUrls: ['./trade-contract.component.scss']
})
export class TradeContractComponent implements OnInit {

  geneticResources: string[] = ['Resource 1','Resource 2','Resource 3','Resource 4','Resource 5','Resource 6','Resource 7','Resource 8'];
  attachments: ResourceFile[] = [];


  constructor() { }

  ngOnInit() {
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
