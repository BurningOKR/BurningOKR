// istanbul ignore file
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { CorporateObjectiveStructureDto } from '../../model/api/structure/corporate-objective-structure.dto';
import { Observable } from 'rxjs';
import { StructureId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class CorporateObjectiveStructureApiService {

  constructor(private api: ApiHttpService) { }

  createForCompany$(companyId: StructureId, dto: CorporateObjectiveStructureDto): Observable<CorporateObjectiveStructureDto> {
    return this.api.postData$(`companies/${companyId}/corporateObjectiveStructures`, dto);
  }
}
