import { Injectable } from '@angular/core';
import { Structure } from '../../shared/model/ui/OrganizationalUnit/structure';
import { Observable, ReplaySubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConvertTopicDraftToTeamService {

  private selectedUnit$: ReplaySubject<Structure> = new ReplaySubject<Structure>(1);

  setSelectedUnit(structure: Structure): void {
    this.selectedUnit$.next(structure);
  }

  getSelectedUnit$(): Observable<Structure> {
    return this.selectedUnit$.asObservable();
  }
}
