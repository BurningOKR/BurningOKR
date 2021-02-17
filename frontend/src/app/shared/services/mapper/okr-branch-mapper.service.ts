import { Injectable } from '@angular/core';
import { OkrBranchApiService } from '../api/okr-branch-api.service';
import { Observable } from 'rxjs';
import { OkrBranch } from '../../model/ui/OrganizationalUnit/okr-branch';
import { map } from 'rxjs/operators';
import { OkrBranchDto } from '../../model/api/OkrUnit/okr-branch.dto';
import { UnitType } from '../../model/api/OkrUnit/unit-type.enum';
import { OkrUnitId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class OkrBranchMapper {

  constructor(private okrBranchApiService: OkrBranchApiService) {
  }

  static mapToOkrBranch(dto: OkrBranchDto): OkrBranch {
    return new OkrBranch(
      dto.okrUnitId,
      dto.unitName,
      dto.objectiveIds,
      dto.label,
      dto.parentUnitId,
      dto.okrChildUnitIds,
      dto.isActive,
      dto.isParentUnitABranch
    );
  }

  static mapToOkrBranchDto(entity: OkrBranch): OkrBranchDto {
    const dto: OkrBranchDto = new OkrBranchDto();
    dto.okrUnitId = entity.id;
    dto.isParentUnitABranch = entity.isParentUnitABranch;
    dto.unitName = entity.name;
    dto.objectiveIds = entity.objectives;
    dto.label = entity.label;
    dto.parentUnitId = entity.parentUnitId;
    dto.okrChildUnitIds = entity.okrChildUnitIds;
    dto.isActive = entity.isActive;
    dto.__okrUnitType = UnitType.OKR_BRANCH;

    return dto;
  }

  createForCompany$(companyId: OkrUnitId, okrBranch: OkrBranch)
    : Observable<OkrBranch> {

    return this.okrBranchApiService
      .createForCompany$(companyId, OkrBranchMapper.mapToOkrBranchDto(okrBranch))
      .pipe(
        map((dto: OkrBranchDto) => OkrBranchMapper.mapToOkrBranch(dto))
      );
  }

  createForOkrBranch$(okrUnitId: OkrUnitId, okrBranch: OkrBranch)
    : Observable<OkrBranch> {

    return this.okrBranchApiService
      .createForOkrBranch$(
        okrUnitId, OkrBranchMapper.mapToOkrBranchDto(okrBranch))
      .pipe(
        map((dto: OkrBranchDto) => OkrBranchMapper.mapToOkrBranch(dto))
      );
  }
}
