import { Component, OnInit } from '@angular/core';
import { MessageService } from 'src/app/services/message.service';
import { BlockchainExplorerService } from 'src/app/services/blockchain-explorer.service';
import { ContractStatus } from '../../model/contract/contractStatus';
import { FormControl } from '@angular/forms';
import { Contract } from '../../model/contract/contract';
import { ContractsService } from '../../services/contracts.service';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { UserService } from '../../services/user.service';
import { DatePipe } from '@angular/common';
import { ContractRole } from 'src/app/model/contract/contractRole';

@Component({
  selector: 'app-blockchain-explorer',
  templateUrl: './blockchain-explorer.component.html',
  styleUrls: ['./blockchain-explorer.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0', display: 'none' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class BlockchainExplorerComponent implements OnInit {

  dataSource: [];

  searchCriteria: string;
  searching: boolean;

  columnsToDisplay = ['date', 'txid'];

  constructor(private blockchainExplorerService: BlockchainExplorerService) { }

  ngOnInit() {
  }

  filter() {
    // if the search criteria is NULL or undefinded we have nothing to do
    if (this.searchCriteria == null) {
      return;
    }

    this.searching = true;
    console.log('Searching for: ' + this.searchCriteria);
    this.blockchainExplorerService.search(this.searchCriteria).subscribe(result => {
      this.searching = false;
      this.dataSource = this.blockchainExplorerService.getAssets();
      console.log(this.dataSource);
    }, error => {
      this.searching = false;
    });

  }

}
