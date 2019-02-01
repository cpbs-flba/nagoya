import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BlockchainExplorerComponent } from './blockchain-explorer.component';

describe('BlockchainExplorerComponent', () => {
  let component: BlockchainExplorerComponent;
  let fixture: ComponentFixture<BlockchainExplorerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BlockchainExplorerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BlockchainExplorerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
