// istanbul ignore file
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { OkrBranchDto } from '../../model/api/OkrUnit/okr-branch.dto';
import { Observable } from 'rxjs';
import { OkrUnitId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class OkrBranchApiService {

  constructor(private api: ApiHttpService) { }

  createForCompany$(companyId: OkrUnitId, dto: OkrBranchDto): Observable<OkrBranchDto> {
    return this.api.postData$(`companies/${companyId}/branch`, dto);
  }

  createForOkrBranch$(unitId: OkrUnitId, dto: OkrBranchDto)
    : Observable<OkrBranchDto> {

    return this.api.postData$(`branch/${unitId}/branch`, dto);
  }
}
