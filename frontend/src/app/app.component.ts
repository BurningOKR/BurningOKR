import { Component } from '@angular/core';
import { AuthenticationService } from './core/auth/services/authentication.service';
import { FetchingService } from './core/services/fetching.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  userLoggedIn: boolean = this.authService.hasValidAccessToken();

  constructor(private authService: AuthenticationService,
              private fetchingService: FetchingService) {
    this.fetchingService.refetchAll(); // Initially fetch all data needed
  }

  checkIfUserIsLoggedIn(): boolean {
    return this.authService.hasValidAccessToken();
  }
}
