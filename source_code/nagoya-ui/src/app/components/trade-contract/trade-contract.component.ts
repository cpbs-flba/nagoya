import { Component, OnInit } from '@angular/core';
import { ResourceFile } from '../../model/resourceFile';
import { ResourceService } from '../../services/resource.service';
import { GeneticResource } from '../../model/geneticResource';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../core';
import { UserService } from 'src/app/services/user.service';
import { Contract } from 'src/app/model/contract/contract';
import { ContractsService } from 'src/app/services/contracts.service';

@Component({
  selector: 'app-trade-contract',
  templateUrl: './trade-contract.component.html',
  styleUrls: ['./trade-contract.component.scss']
})
export class TradeContractComponent implements OnInit {

  contract: Contract = new Contract();
  geneticResources: GeneticResource[];
  attachments: ResourceFile[] = [];
  privateKey: string;

  constructor(
    private contractsService: ContractsService,
    private resourceService: ResourceService,
    private authenticationService: AuthenticationService,
    private userService: UserService,
    private router: Router) { 

      this.contract.sender = this.userService.getUser();
      this.contract.receiver = this.userService.getUser();

    }

  ngOnInit() {
    if (!this.isUserLoggedIn()) {
      this.router.navigate(['login']);
    }
    this.resourceService.getAll().subscribe(response => {
      this.geneticResources = response;
    }, error => {
      //TODO
      console.log(error);
    });
    
  }

  isUserLoggedIn() {
    return this.authenticationService.isAuthenticated();
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

  isPrivateKeyNeeded() {
    return !this.userService.getUser().storePrivateKey;
  }

  removeAttachment(resourceFile) {
    const index = this.attachments.indexOf(resourceFile, 0);
    if (index > -1) {
      this.attachments.splice(index, 1);
    }
  }

}
