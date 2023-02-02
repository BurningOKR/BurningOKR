import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { InitService } from '../../services/init.service';

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
    return this.initService.isInitialized$()
      .pipe(map(initialized => {
        if (initialized) {
          return this.router.createUrlTree(['/']);
        } else {
          return true;
        }
      }));
  }
}
