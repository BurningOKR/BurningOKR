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

  // Todo: P.B. 07.01.2021 Change the path when backend is done with that
  getDepartmentDescriptionById$(id: DepartmentDescriptionId): Observable<OkrDepartmentDescriptionDto> {
    return this.api.getData$<OkrDepartmentDescriptionDto>(`/departments/${id}/topicdescription`);
  }
  putDepartmentDescription$(description: OkrDepartmentDescriptionDto): Observable<OkrDepartmentDescriptionDto> {
    return this.api.putData$(`TODO/${description.descriptionId}`, description);
  }
  postDepartmentDescription$(id: DepartmentDescriptionId,
                             description: OkrDepartmentDescriptionDto): Observable<OkrDepartmentDescriptionDto> {
    return this.api.postData$(`TODO/${id}`, description);
  }
  deleteDepartmentDescription$(id: DepartmentDescriptionId): Observable<boolean> {
    return this.api.deleteData$(`TODO/${id}`);
  }
}
