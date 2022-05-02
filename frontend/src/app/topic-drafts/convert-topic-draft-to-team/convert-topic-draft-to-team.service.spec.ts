import { ConvertTopicDraftToTeamService } from './convert-topic-draft-to-team.service';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { Observable, of, ReplaySubject } from 'rxjs';
import { OkrDepartment } from '../../shared/model/ui/OrganizationalUnit/okr-department';
import { Structure } from '../../shared/model/ui/OrganizationalUnit/structure';

describe('ConvertTopicDraftToTeamService', () => {
  let convertTopicDraftToTeamService: ConvertTopicDraftToTeamService;
  let selectedUnit$: ReplaySubject<Structure>;

  const topicDraftApiServiceMock: any = {
    convertTopicDraftToTeam$: jest.fn()
  };

  const mockStructure1: Structure = getMockStructure(1, 'MockStructure1', 'MockLabel1');

  beforeEach(() => {
    topicDraftApiServiceMock.convertTopicDraftToTeam$.mockReset();
    topicDraftApiServiceMock.convertTopicDraftToTeam$.mockReturnValue(convertTopicDraftToDepartmentMock$);
    convertTopicDraftToTeamService = new ConvertTopicDraftToTeamService(topicDraftApiServiceMock);
    selectedUnit$ = (convertTopicDraftToTeamService as any).selectedUnit$;
  });

  it('should set the Structure correctly', () => {
    selectedUnit$.asObservable().subscribe(structure => expect(structure).toBe(undefined));
    convertTopicDraftToTeamService.setSelectedUnit(mockStructure1);
    selectedUnit$.asObservable().subscribe(structure => expect(structure).toBe(mockStructure1));
  });

  function convertTopicDraftToDepartmentMock$(topicDraft: OkrTopicDraft): Observable<OkrDepartment> {
    return of(new OkrDepartment(1, `Department of ${topicDraft.name}`, [], -1, 'Team', topicDraft.initiatorId, '', [], true,  true));
  }

  function getMockStructure(id: number, name: string, label: string): Structure {
    return new Structure(id, name, label, [], []);
  }
});
