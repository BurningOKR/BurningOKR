import { Component, OnInit } from '@angular/core';
import { ApiHttpService } from '../services/api-http.service';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { AuthenticationService } from '../auth/services/authentication.service';
import { Location } from '@angular/common';
import { take } from 'rxjs/operators';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-error',
  templateUrl: './error.component.html',
  styleUrls: ['./error.component.scss']
})
export class ErrorComponent implements OnInit {
  errors$: Observable<HttpErrorResponse[]>;
  userIsLoggedIn: boolean = false;

  constructor(
    private api: ApiHttpService,
    private router: Router,
    private authentificationService: AuthenticationService,
    private location: Location
  ) {
    this.errors$ = this.api.getErrors$();
  }

  ngOnInit(): void {
    this.userIsLoggedIn = this.authentificationService.hasValidAccessToken();

    this.errors$
      .pipe(
        take(1)
      )
      .subscribe(errors => {
        if (!errors || errors.length === 0) {
          this.router.navigateByUrl('/');
        }
      });
  }

  navigateToCompanies(): void {
    this.router.navigate(['/companies']);
  }

  navigateToPrevious(): void {
    this.location.back();
  }
}
