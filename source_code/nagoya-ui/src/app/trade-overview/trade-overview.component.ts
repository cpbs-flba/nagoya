import { Component, OnInit } from '@angular/core';
import { ContractStatus } from '../model/contract/contractStatus';
import { MessageService } from '../services/message.service';
import { FormControl } from '@angular/forms';
import { Contract } from '../model/contract/contract';
import { ContractsService } from '../services/contracts.service';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { AuthenticationService } from '../core';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-trade-overview',
  templateUrl: './trade-overview.component.html',
  styleUrls: ['./trade-overview.component.scss'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({ height: '0px', minHeight: '0', display: 'none' })),
      state('expanded', style({ height: '*' })),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class TradeOverviewComponent implements OnInit {

  dataSource: Contract[];
  statuses: ContractStatus[];
  selectedStatus: string;

  columnsToDisplay = ['id', 'date', 'status', 'role'];
  expandedElement: Contract;

  dateFrom = new FormControl({ value: null, disabled: false });
  dateUntil = new FormControl({ value: null, disabled: false });

  constructor(//
    public contractsService: ContractsService,
    private userService: UserService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.statuses = [
      { value: 'CREATED', viewValue: this.messageService.getI18nMessage('CONTRACTS.STATUS_CREATED') },
      { value: 'ACCEPTED', viewValue: this.messageService.getI18nMessage('CONTRACTS.STATUS_ACCEPTED') },
      { value: 'CANCELLED', viewValue: this.messageService.getI18nMessage('CONTRACTS.STATUS_CANCELLED') }
    ];
  }

  getUser() {
    return this.userService.getUser();
  }

  filter() {
    this.dataSource = [];

    let param1 = null;
    if (this.dateFrom != null && this.dateFrom.value != null) {
      param1 = this.dateFrom.value;
      param1 = new DatePipe('en-US').transform(new Date(param1), 'yyyy-MM-dd\'T\'HH:mm:ss\'Z\'');
    }

    let param2 = null;
    if (this.dateUntil!= null && this.dateUntil.value != null) {
      param2 = this.dateUntil.value;
      param2 = new DatePipe('en-US').transform(new Date(param2), 'yyyy-MM-dd\'T\'HH:mm:ss\'Z\'');
    }

    let param3 = null;
    if (this.selectedStatus != null) {
      param3 = this.selectedStatus;
    }

    this.contractsService.getAll(param1, param2, param3).subscribe(result => {
      this.dataSource = this.contractsService.getContracts();
    });
  }

}
