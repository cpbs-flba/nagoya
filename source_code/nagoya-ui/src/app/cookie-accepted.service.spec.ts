import { TestBed } from '@angular/core/testing';

import { CookieAcceptedService } from './cookie-accepted.service';

describe('CookieAcceptedService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CookieAcceptedService = TestBed.get(CookieAcceptedService);
    expect(service).toBeTruthy();
  });
});
