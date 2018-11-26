import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { CookieAcceptedService } from '../services/cookie-accepted.service';

@Component({
  selector: 'app-cookies-dialog',
  templateUrl: './cookies-dialog.component.html',
  styleUrls: ['./cookies-dialog.component.css']
})
export class CookiesDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<CookiesDialogComponent>,
    public cookieService: CookieAcceptedService,
    @Inject(MAT_DIALOG_DATA) public data: any) {
  }

  close(): void {
    this.cookieService.cookiesAccepted();
    this.dialogRef.close();
  }

  ngOnInit() {
  }

}

