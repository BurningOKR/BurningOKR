import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { ViewObjective } from '../../model/ui/view-objective';
import { ObjectiveApiService } from '../api/objective-api.service';
import { ObjectiveDto } from '../../model/api/objective.dto';
import { ObjectiveId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class ObjectiveViewMapper {
  constructor(private objectiveApiService: ObjectiveApiService) {}

  private static mapToObjectiveDTO(viewObjective: ViewObjective): ObjectiveDto {
    return {
      title: viewObjective.name,
      description: viewObjective.description,
      remark: viewObjective.remark,
      isActive: viewObjective.isActive,
      parentStructureId: viewObjective.parentStructureId,
      parentObjectiveId: viewObjective.parentObjectiveId
    };
  }

  private static mapToViewObjective(objective: ObjectiveDto): ViewObjective {
    return new ViewObjective(
      objective.id,
      objective.title,
      objective.description,
      objective.remark,
      100,
      objective.keyResultIds,
      objective.isActive,
      objective.parentObjectiveId,
      objective.parentStructureId,
      objective.contactPersonId,
      objective.subObjectiveIds.length,
      objective.review
    );
  }

  getObjectiveById$(id: number): Observable<ViewObjective> {
    return this.objectiveApiService.getObjectiveById$(id)
      .pipe(map(ObjectiveViewMapper.mapToViewObjective));
  }

  getObjectivesForDepartment$(departmentId: number): Observable<ViewObjective[]> {
    return this.objectiveApiService.getObjectivesForDepartment$(departmentId)
      .pipe(
      map((objectiveList: ObjectiveDto[]) => {
        return objectiveList.map(ObjectiveViewMapper.mapToViewObjective);
      })
    );
  }

  postObjectiveForDepartment$(departmentId: number, viewObjective: ViewObjective): Observable<ViewObjective> {
    return this.objectiveApiService
      .postObjectiveForDepartment$(departmentId, ObjectiveViewMapper.mapToObjectiveDTO(viewObjective))
      .pipe(map(ObjectiveViewMapper.mapToViewObjective));
  }

  putObjective$(viewObjective: ViewObjective): Observable<ViewObjective> {
    return this.objectiveApiService
      .putObjective$(ObjectiveViewMapper.mapToObjectiveDTO(viewObjective), viewObjective.id)
      .pipe(map(ObjectiveViewMapper.mapToViewObjective));
  }

  putObjectiveKeyResultSequence$(objectiveId: ObjectiveId, sequenceList: number[]): Observable<number[]> {
    return this.objectiveApiService.putObjectiveKeyResultSequence$(objectiveId, sequenceList);
  }

  deleteObjective$(objectiveId: ObjectiveId): Observable<boolean> {
    return this.objectiveApiService.deleteObjective$(objectiveId);
  }
}
