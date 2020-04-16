import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { CurrentUserService } from '../core/services/current-user.service';

@Injectable({providedIn: 'root'})
export class AdminRoleGuard implements CanActivate {

  constructor(private router: Router,
              private currentUserService: CurrentUserService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    this.currentUserService.isCurrentUserAdmin$()
      .subscribe((isAdmin: boolean) => {
      if (!isAdmin) {
        this.router.navigate(['/companies']);

        return false;
      }
    });

    return true;
  }
}
