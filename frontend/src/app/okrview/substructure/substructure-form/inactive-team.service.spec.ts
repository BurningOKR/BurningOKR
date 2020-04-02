import { TestBed } from '@angular/core/testing';

import { InactiveTeamService } from './inactive-team.service';

describe('InactiveTeamService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: InactiveTeamService = TestBed.get(InactiveTeamService);
    expect(service)
      .toBeTruthy();
  });

});
