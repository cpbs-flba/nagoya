import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CookiesDialogComponent } from './cookies-dialog.component';

describe('CookiesDialogComponent', () => {
  let component: CookiesDialogComponent;
  let fixture: ComponentFixture<CookiesDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CookiesDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CookiesDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
