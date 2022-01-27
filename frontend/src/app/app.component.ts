import { Component } from '@angular/core';
import { DateAdapter } from '@angular/material/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { environment } from '../environments/environment';
import { AuthenticationService } from './core/auth/services/authentication.service';
import { FetchingService } from './core/services/fetching.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  userLoggedIn: boolean = this.authService.hasValidAccessToken();
  isPlayground: boolean = environment.playground;

  constructor(private authService: AuthenticationService,
              private fetchingService: FetchingService,
              private router: Router,
              translateService: TranslateService,
              private dateAdapter: DateAdapter<Date>) {
    this.authService.configure()
      .then(() => {
        this.fetchingService.refetchAll();
      });
    // this language will be used as a fallback when a translation isn't found in the current language
    translateService.setDefaultLang('en');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    translateService.use('en');
    dateAdapter.setLocale(translateService.currentLang);
  }

  checkIfUserIsLoggedIn(): boolean {
    return this.authService.hasValidAccessToken();
  }

  demoGithubButtonHidden(): boolean {
    return !this.router.url.startsWith('/demo');
  }
}
