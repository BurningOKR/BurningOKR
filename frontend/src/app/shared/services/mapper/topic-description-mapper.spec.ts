import { TopicDescriptionMapper } from './topic-description-mapper';
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
    description = new OkrTopicDescription(1, 'DescriptionName', '2', ['2', '3', '4'], ['3', '4'], 'description',
      'Contributes To', 'Delimination', new Date(2021, 1, 1), 'Dependencies', 'Resources', 'Handover Plan');

    descriptionDto = {
      id: 1,
      name: 'DescriptionName',
      initiatorId: '2',
      startTeam: ['2', '3', '4'],
      stakeholders: ['3', '4'],
      description: 'description',
      contributesTo: 'Contributes To',
      delimitation: 'Delimination',
      beginning: [2021, 2, 1],
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
    service = TestBed.inject(TopicDescriptionMapper);

    expect(service)
      .toBeTruthy();
  });

  it('getTopicDescriptionById$ should map', done => {
    service = TestBed.inject(TopicDescriptionMapper);

    service.getTopicDescriptionById$(1)
      .subscribe((departmentDescription: OkrTopicDescription) => {
        expect(departmentDescription)
          .toEqual(description);
        done();
      });
  });

  it('putTopicDescription$ should map', done => {
    service = TestBed.inject(TopicDescriptionMapper);

    service.putTopicDescription$(1, description)
      .subscribe((departmentDescription: OkrTopicDescription) => {
        expect(departmentDescription)
          .toEqual(description);
        done();
      });
  });

  it('putTopicDescription$ should call service', done => {
    service = TestBed.inject(TopicDescriptionMapper);

    service.putTopicDescription$(1, description)
      .subscribe(() => {
        expect(topicDescriptionApiServiceMock.putTopicDescription$)
          .toHaveBeenCalled();
        done();
      });
  });

  it('putTopicDescription$ should map description to descriptionDto', done => {
    service = TestBed.inject(TopicDescriptionMapper);

    service.putTopicDescription$(1, description)
      .subscribe(() => {
        expect(topicDescriptionApiServiceMock.putTopicDescription$)
          .toHaveBeenCalledWith(1, {
            id: 1,
            name: 'DescriptionName',
            initiatorId: '2',
            startTeam: ['2', '3', '4'],
            stakeholders: ['3', '4'],
            description: 'description',
            contributesTo: 'Contributes To',
            delimitation: 'Delimination',
            beginning: [2021, 2, 1],
            dependencies: 'Dependencies',
            resources: 'Resources',
            handoverPlan: 'Handover Plan'
          });
        done();
      });
  });
});
