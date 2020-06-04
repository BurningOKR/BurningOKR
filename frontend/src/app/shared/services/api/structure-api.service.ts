import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable } from 'rxjs';
import { SubStructureDto } from '../../model/api/structure/sub-structure.dto';
import { StructureSchema } from '../../model/ui/structure-schema';

@Injectable({
  providedIn: 'root'
})
export class StructureApiService {

  constructor(private http: ApiHttpService) { }

  getSubStructureById$(structureId: number): Observable<SubStructureDto> {
    return this.http.getData$<SubStructureDto>(`structures/${structureId}`);
  }

  putSubStructure$(structureId: number, subStructure: SubStructureDto): Observable<SubStructureDto> {
    return this.http.putData$<SubStructureDto>(`structures/${structureId}`, subStructure);
  }

  deleteSubStructure$(structureId: number): Observable<boolean> {
    return this.http.deleteData$(`structures/${structureId}`);
  }

  getStructureSchemaByStructureId$(structureId: number): Observable<StructureSchema[]> {
    return this.http.getData$<StructureSchema[]>(`structures/${structureId}/structure`);
  }
}
