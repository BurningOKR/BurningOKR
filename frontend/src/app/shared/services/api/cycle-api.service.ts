// istanbul ignore file
import { Injectable } from '@angular/core';
import { ApiHttpService } from '../../../core/services/api-http.service';
import { Observable } from 'rxjs';
import { CycleDto } from '../../model/api/cycle.dto';
import { CycleId } from '../../model/id-types';

@Injectable({
  providedIn: 'root'
})
export class CycleApiService {

  constructor(private api: ApiHttpService) {
  }

  getCycleById$(id: CycleId): Observable<CycleDto> {
    return this.api.getData$(`cycles/${id}`);
  }

  getAllCycles$(): Observable<CycleDto[]> {
    return this.api.getData$(`cycles`);
  }

  postCycle$(cycle: CycleDto): Observable<CycleDto> {
    return this.api.postData$(`cycles/`, cycle);
  }

  putCycleById$(cycle: CycleDto): Observable<CycleDto> {
    return this.api.putData$(`cycles/${cycle.id}`, cycle);
  }

  cloneCycleFromCycleId$(formerCycleId: CycleId, cycle: CycleDto): Observable<CycleDto> {
    return this.api.postData$(`clonecycle/${formerCycleId}`, cycle);
  }

  deleteCycleById$(cycleId: CycleId): Observable<boolean> {
    return this.api.deleteData$(`cycles/${cycleId}`);
  }
}
