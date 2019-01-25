import { Component, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { CookiesDialogComponent } from '../cookies-dialog/cookies-dialog.component';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {

  constructor(public dialog: MatDialog) { }

  openDialog(element: any): void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;

    this.dialog.open(CookiesDialogComponent, dialogConfig);
  }

  ngOnInit() {
  }

}



