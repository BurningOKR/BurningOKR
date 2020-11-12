import { TestBed } from '@angular/core/testing';
import { KeyResultMilestoneMapper } from './key-result-milestone.mapper';

describe('KeyResultMilestone.MapperService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: KeyResultMilestoneMapper = TestBed.get(KeyResultMilestoneMapper);
    expect(service)
      .toBeTruthy();
  });
});
