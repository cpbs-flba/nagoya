import { Component, OnInit } from '@angular/core';
import { ContractStatus } from '../../model/contract/contractStatus';
import { MessageService } from '../../services/message.service';
import { FormControl } from '@angular/forms';
import { Contract } from '../../model/contract/contract';
import { ContractsService } from '../../services/contracts.service';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { UserService } from '../../services/user.service';
import { DatePipe } from '@angular/common';
import { ContractRole } from 'src/app/model/contract/contractRole';

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

  roles: ContractRole[];
  selectedRole: string;

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
    this.roles = [
      { value: 'SENDER', viewValue: this.messageService.getI18nMessage('CONTRACTS.SENDER') },
      { value: 'RECEIVER', viewValue: this.messageService.getI18nMessage('CONTRACTS.RECEIVER') }
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
      param1 = new DatePipe('en-US').transform(new Date(param1), 'yyyy-MM-dd\'T\'00:00:00\'Z\'');
    }

    let param2 = null;
    if (this.dateUntil != null && this.dateUntil.value != null) {
      param2 = this.dateUntil.value;
      param2 = new DatePipe('en-US').transform(new Date(param2), 'yyyy-MM-dd\'T\'23:59:59\'Z\'');
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
