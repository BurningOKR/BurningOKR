import {ConvertTopicDraftToTeamService} from './convert-topic-draft-to-team.service';
import {Observable, of, ReplaySubject} from 'rxjs';
import {OkrDepartment} from '../../shared/model/ui/OrganizationalUnit/okr-department';
import {Structure} from '../../shared/model/ui/OrganizationalUnit/structure';
import {UnitType} from "../../shared/model/api/OkrUnit/unit-type.enum";

describe('ConvertTopicDraftToTeamService', () => {
  let convertTopicDraftToTeamService: ConvertTopicDraftToTeamService;
  let selectedUnit$: ReplaySubject<Structure>;

  const topicDraftApiServiceMock: any = {
    convertTopicDraftToTeam$: convertTopicDraftToDepartmentMock$,
  };

  const mockStructure1: Structure = getMockStructure(1, 'MockStructure1', 'MockLabel1');

  beforeEach(() => {
    convertTopicDraftToTeamService = new ConvertTopicDraftToTeamService(topicDraftApiServiceMock);
    selectedUnit$ = (convertTopicDraftToTeamService as any).selectedUnit$;
  });

  it('should set the Structure correctly', done => {
    selectedUnit$.asObservable().subscribe(structure => {
      expect(structure).toBe(mockStructure1);
      done();
    });
    convertTopicDraftToTeamService.setSelectedUnit(mockStructure1);
  });

  it('should get the selected Structure correctly', done => {
    convertTopicDraftToTeamService.getSelectedUnit$().subscribe(structure => {
      expect(structure).toBe(mockStructure1);
      done();
    });
    selectedUnit$.next(mockStructure1);
  });

  it('should call the convertToTeam-Endpoint correctly', done => {
    selectedUnit$.next(mockStructure1);
    convertTopicDraftToTeamService.convertTopicDraftToTeam$(3)
      .subscribe(actual => {
        expect(actual.id).toBe(1);
        expect(actual.parentUnitId).toBe(1);
        expect(actual.name).toBe('Department of 3');
        done();
      });
  });

  function convertTopicDraftToDepartmentMock$(topicDraftId: number, okrUnitId: number): Observable<OkrDepartment> {
    const x: OkrDepartment = {
      id: 1,
      name: `Department of ${topicDraftId}`,
      objectives: [],
      parentUnitId: okrUnitId,
      type: UnitType.DEPARTMENT,
      label: 'Team',
      okrTopicSponsorId: '',
      okrMasterId: '',
      okrMemberIds: [],
      isParentUnitABranch: true,
      isActive: true

    }
    return of(x);
  }

  function getMockStructure(id: number, name: string, label: string): Structure {
    return {
      id,
      name,
      label,
      objectives: [],
      substructures: []
    }
  }
});
