import { TestBed } from '@angular/core/testing';

import { ServerconfigService } from './serverconfig.service';

describe('ServerconfigService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ServerconfigService = TestBed.get(ServerconfigService);
    expect(service).toBeTruthy();
  });
});
