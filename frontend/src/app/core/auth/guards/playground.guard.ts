import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot } from '@angular/router';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PlaygroundGuard implements CanActivate {
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    return !environment.playground;
  }

}
