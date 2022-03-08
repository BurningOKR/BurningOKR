import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from '../environments/environment';
import { AuthenticationService } from './core/auth/services/authentication.service';
import { FetchingService } from './core/services/fetching.service';
import {OkrTranslationHelperService} from './shared/services/helper/okr-translation-helper.service';

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
              private OkrTranslationHelper: OkrTranslationHelperService) {
    this.authService.configure()
      .then(() => {
        this.fetchingService.refetchAll();
      });
    this.OkrTranslationHelper.initializeTranslationOnStartup();
  }

  checkIfUserIsLoggedIn(): boolean {
    return this.authService.hasValidAccessToken();
  }

  demoGithubButtonHidden(): boolean {
    return !this.router.url.startsWith('/demo');
  }
}
