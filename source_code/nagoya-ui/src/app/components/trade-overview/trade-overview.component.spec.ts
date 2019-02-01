import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TradeOverviewComponent } from './trade-overview.component';

describe('TradeOverviewComponent', () => {
  let component: TradeOverviewComponent;
  let fixture: ComponentFixture<TradeOverviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TradeOverviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TradeOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
