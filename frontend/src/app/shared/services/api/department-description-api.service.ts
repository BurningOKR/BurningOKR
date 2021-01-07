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
  getDescriptionById$(id: DepartmentDescriptionId): Observable<OkrDepartmentDescriptionDto> {
    return this.api.getData$<OkrDepartmentDescriptionDto>(`/departments/${id}/topicdescription`);
  }
  putDescription$(description: OkrDepartmentDescriptionDto): Observable<OkrDepartmentDescriptionDto> {
    return this.api.putData$(`TODO/${description.descriptionId}`, description);
  }
  postDescription$(id: DepartmentDescriptionId, description: OkrDepartmentDescriptionDto): Observable<OkrDepartmentDescriptionDto> {
    return this.api.postData$(`TODO/${id}`, description);
  }
  deleteDescription$(id: DepartmentDescriptionId): Observable<boolean> {
    return this.api.deleteData$(`TODO/${id}`);
  }
}
