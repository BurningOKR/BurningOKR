import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { DepartmentDescriptionId } from '../../model/id-types';
import { OkrDepartmentDescriptionDto } from '../../model/api/OkrUnit/okr-department-description.dto';

@Injectable({
  providedIn: 'root'
})
export class DepartmentDescriptionApiService {
  constructor(private api: ApiHttpService) {}

  getDepartmentDescriptionById$(id: DepartmentDescriptionId): Observable<OkrDepartmentDescriptionDto> {
    return this.api.getData$<OkrDepartmentDescriptionDto>(`/departments/${id}/topicdescription`);
  }
  putDepartmentDescription$(descriptionId: DepartmentDescriptionId , descriptionDto:
    OkrDepartmentDescriptionDto): Observable<OkrDepartmentDescriptionDto> {
    return this.api.putData$(`/departments/${descriptionId}/topicdescription`, descriptionDto);
  }
}
