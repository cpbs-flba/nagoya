import { TestBed } from '@angular/core/testing';

import { IncompatibleBrowserService } from './incompatible-browser.service';

describe('IncompatibleBrowserService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: IncompatibleBrowserService = TestBed.get(IncompatibleBrowserService);
    expect(service).toBeTruthy();
  });
});
