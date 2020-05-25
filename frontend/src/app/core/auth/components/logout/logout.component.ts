import { Component, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { Router } from '@angular/router';
import { OAuthFrontendDetailsService } from '../../services/o-auth-frontend-details.service';
import { take } from 'rxjs/operators';
import { Consts } from '../../../../shared/consts';

@Component({
  selector: 'app-login',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit {

  constructor(
    private oAuthService: OAuthService,
    private router: Router,
    private oAuthDetails: OAuthFrontendDetailsService
  ) {
  }

  ngOnInit(): void {
    this.oAuthService.logOut();
    this.oAuthDetails.getAuthType$()
      .pipe(take(1))
      .subscribe(authType => {
        if (authType === Consts.AUTHTYPE_LOCAL) {
          this.router.navigate(['auth', 'login']);
        }
      });
  }
}
