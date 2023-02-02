import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable } from 'rxjs';
import { StructureDto } from '../../model/api/OkrUnit/structure.dto';

@Injectable({
  providedIn: 'root',
})
export class StructureApiService {

  constructor(
    private api: ApiHttpService,
  ) {
  }

  getSchemaOfAllExistingStructures$(): Observable<StructureDto[]> {
    return this.api.getData$('structure');
  }

  getSchemaOfAllActiveStructuresWithCycleName$(): Observable<StructureDto[]> {
    return this.api.getData$('structure?active=true&attachCycleName=true');
  }
}
