import { ApiHttpService } from '../../../core/services/api-http.service';
import { StructureId } from '../../model/id-types';
import { Observable } from 'rxjs';
import { SubStructureDto } from '../../model/api/structure/sub-structure.dto';
import { DepartmentDto } from '../../model/api/structure/department.dto';

export class StructureApiService {
  constructor(private api: ApiHttpService) {
  }

  getStructureByStructureId(id: StructureId): Observable<SubStructureDto> {
    return this.api.getData$<SubStructureDto>(`/structures/${id}`);
  }
}
