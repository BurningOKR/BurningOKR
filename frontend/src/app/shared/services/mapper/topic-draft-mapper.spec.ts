import { TestBed } from '@angular/core/testing';
import { TopicDraftApiService } from '../api/topic-draft-api.service';
import { TopicDraftMapper } from './topic-draft-mapper';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthenticationService } from '../../../core/auth/services/authentication.service';
import { I18n } from '@ngx-translate/i18n-polyfill';
import { MaterialTestingModule } from '../../../testing/material-testing.module';
import { LoggerTestingModule } from 'ngx-logger/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { OkrTopicDraft } from '../../model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { OkrTopicDraftDto } from '../../model/api/OkrUnit/okr-topic-draft.dto';
import { status } from '../../model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { User } from '../../model/api/user';
import { of } from 'rxjs';

const userMock: User = new User();
const i18nMock: any = jest.fn();
const topicDraftApiServiceMock: any = {
    postTopicDraftForCompany$: jest.fn(),
    postTopicDraftForOkrBranch$: jest.fn(),
    getAllTopicDrafts$: jest.fn(),
    deleteTopicDraft$: jest.fn()
};

let topicDraftMapper: TopicDraftMapper;
let topicDraft: OkrTopicDraft;
let topicDraftDto: OkrTopicDraftDto;

describe('TopicDraftMapper', () => {
    beforeEach(() => TestBed.configureTestingModule({
        imports: [HttpClientTestingModule, MaterialTestingModule, LoggerTestingModule, RouterTestingModule],
        providers: [
            {provide: TopicDraftApiService, useValue: topicDraftApiServiceMock},
            {provide: AuthenticationService, useValue: {}},
            {provide: I18n, useValue: i18nMock}
        ]
    }));

    beforeEach(() => {
        topicDraft = new OkrTopicDraft(1, status.draft, userMock, 3, 'TopicDraft', 'initiatorId', ['5', '6'],
            ['5', '6'], 'Acceptance Criteria', 'Contributes to', 'Delimitation',
            new Date(2021, 1, 1), 'Dependencies', 'Resources', 'Handover plan');
        topicDraftDto = {
            okrParentUnitId: 1,
            currentStatus: status.draft,
            initiator: userMock,
            id: 3,
            name: 'TopicDraft',
            initiatorId: 'initiatorId',
            startTeam: ['5', '6'],
            stakeholders: ['5', '6'],
            description: 'Acceptance Criteria',
            contributesTo: 'Contributes to',
            delimitation: 'Delimitation',
            beginning: [2021, 2, 1],
            dependencies: 'Dependencies',
            resources: 'Resources',
            handoverPlan: 'Handover plan'
        };
        topicDraftApiServiceMock.postTopicDraftForCompany$.mockReset();
        topicDraftApiServiceMock.postTopicDraftForCompany$.mockReturnValue(of(topicDraftDto));
        topicDraftApiServiceMock.postTopicDraftForOkrBranch$.mockReset();
        topicDraftApiServiceMock.postTopicDraftForOkrBranch$.mockReturnValue(of(topicDraftDto));
        topicDraftApiServiceMock.getAllTopicDrafts$.mockReset();
        topicDraftApiServiceMock.getAllTopicDrafts$.mockReturnValue(of([topicDraftDto]));
        topicDraftApiServiceMock.deleteTopicDraft$.mockReset();
        topicDraftApiServiceMock.deleteTopicDraft$.mockReturnValue(of(true));
    });

    it('should be created', () => {
        topicDraftMapper = TestBed.inject(TopicDraftMapper);

        expect(topicDraftMapper)
            .toBeTruthy();
    });

    it('postTopicDraftForCompany$ should call service', done => {
        topicDraftMapper = TestBed.inject(TopicDraftMapper);

        topicDraftMapper.postTopicDraftForCompany$(0, topicDraft)
            .subscribe(() => {
                expect(topicDraftApiServiceMock.postTopicDraftForCompany$)
                    .toHaveBeenCalled();
                done();
            });
    });

    it('postTopicDraftForCompany$ should map', done => {
        topicDraftMapper = TestBed.inject(TopicDraftMapper);

        topicDraftMapper.postTopicDraftForCompany$(0, topicDraft)
            .subscribe((okrTopicDraft: OkrTopicDraft) => {
                expect(okrTopicDraft)
                    .toEqual(topicDraft);
                done();
            });
    });

    it('postTopicDraftForOkrBranch$ should call servuce', done => {
        topicDraftMapper = TestBed.inject(TopicDraftMapper);

        topicDraftMapper.postTopicDraftForOkrBranch$(0, topicDraft)
            .subscribe(() => {
                expect(topicDraftApiServiceMock.postTopicDraftForOkrBranch$)
                    .toHaveBeenCalled();
                done();
            });
    });

    it('postTopicDraftForOkrBranch$ should map', done => {
        topicDraftMapper = TestBed.inject(TopicDraftMapper);

        topicDraftMapper.postTopicDraftForOkrBranch$(0, topicDraft)
            .subscribe((okrTopicDraft: OkrTopicDraft) => {
                expect(okrTopicDraft)
                    .toEqual(topicDraft);
                done();
            });
    });

    it('getAllTopicDrafts$ should call service', done => {
        topicDraftMapper = TestBed.inject(TopicDraftMapper);

        topicDraftMapper.getAllTopicDrafts$()
            .subscribe(() => {
                expect(topicDraftApiServiceMock.getAllTopicDrafts$)
                    .toHaveBeenCalled();
                done();
            });
    });

    it('getAllTopicDrafts$ should map topicDraftDto to topicDraft ', done => {
        topicDraftMapper = TestBed.inject(TopicDraftMapper);

        topicDraftMapper.getAllTopicDrafts$()
            .subscribe((okrTopicDraft: OkrTopicDraft[]) => {
                expect(okrTopicDraft[0])
                    .toEqual(topicDraft);
                done();
            });
    });

    it('deleteTopicDraft$ should call service with id = 3', done => {
        topicDraftMapper.deleteTopicDraft$(3)
            .subscribe(() => {
                expect(topicDraftApiServiceMock.deleteTopicDraft$)
                    .toHaveBeenCalledWith(3);
                done();
            });
    });
});
