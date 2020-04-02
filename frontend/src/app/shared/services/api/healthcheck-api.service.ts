import {Injectable} from "@angular/core";
import {ApiHttpService} from "../../../core/services/api-http.service";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})

export class HealthcheckApiService{

  constructor(private api: ApiHttpService) {
  }

  public isAlive$(): Observable<boolean>
  {
    return this.api.getData$(`isAlive`);
  }
}
