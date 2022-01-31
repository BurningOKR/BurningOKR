import { TestBed } from '@angular/core/testing';

import { OkrTranslationHelperService } from './okr-translation-helper.service';

describe('OkrTranslationService', () => {
  let service: OkrTranslationHelperService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OkrTranslationHelperService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
