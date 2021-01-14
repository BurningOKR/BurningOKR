import { TopicDescriptionMapper } from './topic-description-mapper.service';
import { TestBed } from '@angular/core/testing';
import { OkrTopicDescriptionDto } from '../../model/api/OkrUnit/okr-topic-description.dto';
import { TopicDescriptionApiService } from '../api/topic-description-api.service';
import { OkrTopicDescription } from '../../model/ui/OrganizationalUnit/okr-topic-description';
import { of } from 'rxjs';

const topicDescriptionApiServiceMock: any = {
  getTopicDescriptionById$: jest.fn(),
  putTopicDescription$: jest.fn()
};

let service: TopicDescriptionMapper;
let description: OkrTopicDescription;
let descriptionDto: OkrTopicDescriptionDto;

describe('TopicDescriptionMapper', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      { provide: TopicDescriptionApiService, useValue: topicDescriptionApiServiceMock }
    ]
  }));

  beforeEach(() => {
    description = new OkrTopicDescription(1, 'DescriptionName', '2', ['2', '3', '4'], ['3', '4'], 'acceptanceCriteria',
      'Contributes To', 'Delimination', new Date(1, 1, 2021), 'Dependencies', 'Resources', 'Handover Plan');

    descriptionDto = {
      descriptionId: 1,
      name: 'DescriptionName',
      initiatorId: '2',
      startTeam: ['2', '3', '4'],
      stakeholders: ['3', '4'],
      acceptanceCriteria: 'acceptanceCriteria',
      contributesTo: 'Contributes To',
      delimitation: 'Delimination',
      beginning: new Date(1, 1, 2021),
      dependencies: 'Dependencies',
      resources: 'Resources',
      handoverPlan: 'Handover Plan'
    };

    topicDescriptionApiServiceMock.getTopicDescriptionById$.mockReset();
    topicDescriptionApiServiceMock.getTopicDescriptionById$.mockReturnValue(of(descriptionDto));
    topicDescriptionApiServiceMock.putTopicDescription$.mockReset();
    topicDescriptionApiServiceMock.putTopicDescription$.mockReturnValue(of(descriptionDto));
  });

  it('should be created', () => {
    service = TestBed.get(TopicDescriptionMapper);

    expect(service)
      .toBeTruthy();
  });

  it('getTopicDescriptionById$ should map', done => {
    service.getTopicDescriptionById$(1)
      .subscribe((departmentDescription: OkrTopicDescription) => {
        expect(departmentDescription)
          .toEqual(description);
        done();
      });
  });

  it('putTopicDescription$ should map', done => {
    service = TestBed.get(TopicDescriptionMapper);

    service.putTopicDescription$(description)
      .subscribe((departmentDescription: OkrTopicDescription) => {
        expect(departmentDescription)
          .toEqual(description);
        done();
      });
  });

  it('putTopicDescription$ should call service', done => {
    service = TestBed.get(TopicDescriptionMapper);

    service.putTopicDescription$(description)
      .subscribe(() => {
        expect(topicDescriptionApiServiceMock.putTopicDescription$)
          .toHaveBeenCalled();
        done();
      });
  });

  it('putTopicDescription$ should map description to descriptionDto', done => {
    service = TestBed.get(TopicDescriptionMapper);

    service.putTopicDescription$(description)
      .subscribe(() => {
        expect(topicDescriptionApiServiceMock.putTopicDescription$)
          .toHaveBeenCalledWith(1, {
            descriptionId: 1,
            name: 'DescriptionName',
            initiatorId: '2',
            startTeam: ['2', '3', '4'],
            stakeholders: ['3', '4'],
            acceptanceCriteria: 'acceptanceCriteria',
            contributesTo: 'Contributes To',
            delimitation: 'Delimination',
            beginning: new Date(1, 1, 2021),
            dependencies: 'Dependencies',
            resources: 'Resources',
            handoverPlan: 'Handover Plan'
          });
        done();
      });
  });
});
