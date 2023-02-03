import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { InitService } from '../../services/init.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class NotInitiliazedGuard implements CanActivate {
  constructor(
    private router: Router,
    private initService: InitService,
  ) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot,
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.initService.isInitialized$()
      .pipe(map(initialized => {
        if (initialized) {
          return true;
        } else {
          return this.router.createUrlTree(['auth', 'init']);
        }
      }));
  }
}
