import { ConvertTopicDraftToTeamService } from './convert-topic-draft-to-team.service';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { Observable, of } from 'rxjs';
import { OkrDepartment } from '../../shared/model/ui/OrganizationalUnit/okr-department';

describe('ConvertTopicDraftToTeamService', () => {
  let convertTopicDraftToTeamService: ConvertTopicDraftToTeamService;
  const topicDraftApiServiceMock: any = {
    convertTopicDraftToTeam$: jest.fn()
  };

  beforeEach(() => {
    topicDraftApiServiceMock.convertTopicDraftToTeam$.mockReset();
    topicDraftApiServiceMock.convertTopicDraftToTeam$.mockReturnValue(convertTopicDraftToDepartmentMock$);
    convertTopicDraftToTeamService = new ConvertTopicDraftToTeamService(topicDraftApiServiceMock);
  });

  it('should be created', () => {
    expect(convertTopicDraftToTeamService).toBeTruthy();
  });

  function convertTopicDraftToDepartmentMock$(topicDraft: OkrTopicDraft): Observable<OkrDepartment> {
    return of(new OkrDepartment(1, `Department of ${topicDraft.name}`, [], -1, 'Team', topicDraft.initiatorId, '', [], true,  true));
  }
});
