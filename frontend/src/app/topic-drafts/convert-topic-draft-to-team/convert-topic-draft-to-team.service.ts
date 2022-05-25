import { Injectable } from '@angular/core';
import { Structure } from '../../shared/model/ui/OrganizationalUnit/structure';
import { Observable, ReplaySubject } from 'rxjs';
import { OkrDepartment } from '../../shared/model/ui/OrganizationalUnit/okr-department';
import { TopicDraftApiService } from '../../shared/services/api/topic-draft-api.service';
import { switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ConvertTopicDraftToTeamService {

  private selectedUnit$: ReplaySubject<Structure> = new ReplaySubject<Structure>(1);

  constructor(private topicDraftApiService: TopicDraftApiService) {
  }

  setSelectedUnit(structure: Structure): void {
    this.selectedUnit$.next(structure);
  }

  getSelectedUnit$(): Observable<Structure> {
    return this.selectedUnit$.asObservable();
  }

  convertTopicDraftToTeam$(topicDraftId: number): Observable<OkrDepartment> {
    return this.selectedUnit$.asObservable().pipe(switchMap(
      okrUnit => this.topicDraftApiService.convertTopicDraftToTeam$(topicDraftId, okrUnit.id)
    ));
  }
}
