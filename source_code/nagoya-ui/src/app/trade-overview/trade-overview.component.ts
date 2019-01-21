import { Component, OnInit } from '@angular/core';
import { ContractStatus } from '../model/contract/contractStatus';
import { MessageService } from '../services/message.service';
import { FormControl } from '@angular/forms';
import { Contract } from '../model/contract/contract';
import { ContractsService } from '../services/contracts.service';
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-trade-overview',
  templateUrl: './trade-overview.component.html',
  styleUrls: ['./trade-overview.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0', display: 'none'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class TradeOverviewComponent implements OnInit {

  dataSource: Contract[];
  statuses: ContractStatus[];

  columnsToDisplay = ['id', 'date', 'status'];
  expandedElement: Contract;

  date = new FormControl({ value: null, disabled: false });

  constructor(//
    public contractsService: ContractsService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.statuses = [
      { value: 'CREATED', viewValue: this.messageService.getI18nMessage('CONTRACTS.STATUS_CREATED') },
      { value: 'ACCEPTED', viewValue: this.messageService.getI18nMessage('CONTRACTS.STATUS_ACCEPTED') },
      { value: 'CANCELLED', viewValue: this.messageService.getI18nMessage('CONTRACTS.STATUS_CANCELLED') }
    ];
  }

  filter() {
    this.contractsService.getAll().subscribe(result => {
      this.dataSource = this.contractsService.getContracts();
    });
  }

}
