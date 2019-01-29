import { Component, OnInit } from '@angular/core';
import { ResourceFile } from '../../model/resourceFile';
import { ResourceService } from '../../services/resource.service';
import { GeneticResource } from '../../model/geneticResource';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../core';
import { UserService } from 'src/app/services/user.service';
import { Contract } from 'src/app/model/contract/contract';
import { ContractsService } from 'src/app/services/contracts.service';
import { ContractResource } from 'src/app/model/contract/contractResource';
import { MessageService } from 'src/app/services/message.service';

@Component({
  selector: 'app-trade-contract',
  templateUrl: './trade-contract.component.html',
  styleUrls: ['./trade-contract.component.scss']
})
export class TradeContractComponent implements OnInit {

  geneticResources: GeneticResource[];
  attachments: ResourceFile[] = [];

  contractResources = new Array();
  selectedContractResources: ContractResource[];

  privateKey: string;
  emailReceiver: string;

  constructor(
    private contractsService: ContractsService,
    private resourceService: ResourceService,
    private messageService: MessageService,
    private userService: UserService,
    private router: Router) {
  }

  ngOnInit() {
    this.resourceService.getAll().subscribe(response => {
      this.geneticResources = response;
      this.geneticResources.forEach(element => {
        let cr: ContractResource = {
          amount: null,
          geneticResource: element,
          measuringUnit: ''
        };
        this.contractResources.push(cr);
      });
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

  createContract() {
    let contract: Contract = {
      id: null,
      sender: this.userService.getUser(),
      receiver: {
        email: this.emailReceiver,
        password: null,
        passwordConfirmation: null,
        personType: null,
        address: null,
        keys: null,
        storePrivateKey: null
      },

      conclusionDate: null,
      contractResources: this.selectedContractResources,
      token: null
    };

    this.contractsService.create(contract, this.privateKey).subscribe(result => {
      this.messageService.displayInfoMessage('CONTRACTS.INFO.CREATED');
      this.router.navigate(['home']);
    }, error => {
      if (error.error== 'ERROR_SENDER_1') {
        this.messageService.displayErrorMessage('CONTRACTS.ERROR.INVALID_COUNTERPARTY');
      }
      if (error.error== 'ERROR_RESOURCE_1') {
        this.messageService.displayErrorMessage('CONTRACTS.ERROR.INVALID_RESOURCE_SELECTION');
      }

      if (error.status == 403) {
        this.messageService.displayErrorMessage('CONTRACTS.ERROR.FORBIDDEN');
      } else if (error.status === 412) {
        this.messageService.displayErrorMessage('CONTRACTS.ERROR.WRONG_PK');
      }
    });
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
