import { TestBed } from '@angular/core/testing';

import { BlockchainExplorerService } from './blockchain-explorer.service';

describe('BlockchainExplorerService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BlockchainExplorerService = TestBed.get(BlockchainExplorerService);
    expect(service).toBeTruthy();
  });
});
