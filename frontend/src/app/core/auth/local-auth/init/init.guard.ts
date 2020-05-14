import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { InitService } from '../../../services/init.service';
import { INIT_STATE_NAME } from '../../../../shared/model/api/init-state';

@Injectable({
  providedIn: 'root'
})

export class InitGuard implements CanActivate {
  constructor(private router: Router,
              private initService: InitService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.initService.getInitState$()
      .pipe(map(receivedInitState => {
        if (receivedInitState.initState === INIT_STATE_NAME.INITIALIZED) {
          this.router.navigate(['auth', 'login']);
        } else {
          return true;
        }
      }));
  }
}
