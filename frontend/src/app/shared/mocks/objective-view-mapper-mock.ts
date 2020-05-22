import { Observable, of } from 'rxjs';
import { ViewObjective } from '../model/ui/view-objective';
import { map } from 'rxjs/operators';
import { ObjectiveDto } from '../model/api/objective.dto';
import { ObjectiveId } from '../model/id-types';

export class ObjectiveViewMapperMock {
  getObjectiveById$(id: number): Observable<ViewObjective> {
    return of();
  }

  getObjectivesForDepartment$(departmentId: number): Observable<ViewObjective[]> {
    return of();

  }

  postObjectiveForDepartment$(departmentId: number, viewObjective: ViewObjective): Observable<ViewObjective> {
    return of();
  }

  putObjective$(viewObjective: ViewObjective): Observable<ViewObjective> {
    return of();
  }

  putObjectiveKeyResultSequence$(objectiveId: ObjectiveId, sequenceList: number[]): Observable<number[]> {
    return of();
  }

  deleteObjective$(objectiveId: ObjectiveId): Observable<boolean> {
    return of();
  }
}
