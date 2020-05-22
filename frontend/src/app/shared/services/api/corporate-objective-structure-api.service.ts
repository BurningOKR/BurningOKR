// istanbul ignore file
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { CorporateObjectiveStructureDto } from '../../model/api/corporate-objective-structure.dto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CorporateObjectiveStructureApiService {

  constructor(private api: ApiHttpService) { }

  getById$(id: number): Observable<CorporateObjectiveStructureDto> {
    return this.api.getData$(`corporateObjectiveStructure/${id}`);
  }

  create$(dto: CorporateObjectiveStructureDto): Observable<CorporateObjectiveStructureDto> {
    return this.api.postData$(`corporateObjectiveStructure`, dto);
  }

  update$(id: number, dto: CorporateObjectiveStructureDto): Observable<CorporateObjectiveStructureDto> {
    return this.api.putData$(`corporateObjectiveStructure/${id}`, dto);
  }

  delete$(id: number): Observable<boolean> {
    return this.api.deleteData$(`corporateObjectiveStructure/${id}`);
  }
}
