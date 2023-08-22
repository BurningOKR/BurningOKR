import { Injectable } from '@angular/core';
import { OkrBranchApiService } from '../api/okr-branch-api.service';
import { Observable } from 'rxjs';
import { OkrBranch } from '../../model/ui/OrganizationalUnit/okr-branch';
import { map } from 'rxjs/operators';
import { OkrBranchDto } from '../../model/api/OkrUnit/okr-branch.dto';
import { UnitType } from '../../model/api/OkrUnit/unit-type.enum';
import { OkrUnitId } from '../../model/id-types';

@Injectable({
  providedIn: 'root',
})
export class OkrBranchMapper {

  constructor(private okrBranchApiService: OkrBranchApiService) {
  }

  static mapToOkrBranch(dto: OkrBranchDto): OkrBranch {
    return {
      type: UnitType.BRANCH,
      id: dto.okrUnitId,
      name: dto.unitName,
      photo: dto.photo,
      objectives: dto.objectiveIds,
      label: dto.label,
      parentUnitId: dto.parentUnitId,
      okrChildUnitIds: dto.okrChildUnitIds,
      isActive: dto.isActive,
      isParentUnitABranch: dto.isParentUnitABranch,
    };
  }

  static mapToOkrBranchDto(entity: OkrBranch): OkrBranchDto {
    return {
      okrUnitId: entity.id,
      isParentUnitABranch: entity.isParentUnitABranch,
      unitName: entity.name,
      photo: entity.photo,
      objectiveIds: entity.objectives,
      label: entity.label,
      parentUnitId: entity.parentUnitId,
      okrChildUnitIds: entity.okrChildUnitIds,
      isActive: entity.isActive,
      __okrUnitType: UnitType.BRANCH,
    };
  }

  createForCompany$(companyId: OkrUnitId, okrBranch: OkrBranch): Observable<OkrBranch> {

    return this.okrBranchApiService
      .createForCompany$(companyId, OkrBranchMapper.mapToOkrBranchDto(okrBranch))
      .pipe(
        map((dto: OkrBranchDto) => OkrBranchMapper.mapToOkrBranch(dto)),
      );
  }

  createForOkrBranch$(okrUnitId: OkrUnitId, okrBranch: OkrBranch): Observable<OkrBranch> {

    return this.okrBranchApiService
      .createForOkrBranch$(
        okrUnitId, OkrBranchMapper.mapToOkrBranchDto(okrBranch))
      .pipe(
        map((dto: OkrBranchDto) => OkrBranchMapper.mapToOkrBranch(dto)),
      );
  }
}
