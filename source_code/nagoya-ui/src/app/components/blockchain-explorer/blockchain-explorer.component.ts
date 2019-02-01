import { Component, OnInit } from '@angular/core';
import { MessageService } from 'src/app/services/message.service';
import { BlockchainExplorerService } from 'src/app/services/blockchain-explorer.service';
import { animate, state, style, transition, trigger } from '@angular/animations';

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
  noresults: boolean;

  columnsToDisplay = ['date', 'txid'];

  constructor(
    private blockchainExplorerService: BlockchainExplorerService,
    private messageService: MessageService,
  ) { }

  ngOnInit() {
  }

  filter() {
    this.noresults = false;

    // if the search criteria is NULL or undefinded we have nothing to do
    if (!this.searchCriteria) {
      this.messageService.displayErrorMessage('BLOCKCHAIN_EXPLORER.SEARCH_CRITERIA_MISSING');
      return;
    }

    this.searching = true;
    console.log('Searching for: ' + this.searchCriteria);
    this.blockchainExplorerService.search(this.searchCriteria).subscribe(result => {
      this.searching = false;
      this.dataSource = this.blockchainExplorerService.getAssets();
      if (this.dataSource.length == 0) {
        this.noresults = true;
      }
    }, error => {
      this.searching = false;
    });

  }

}
