import { Component, EventEmitter, OnInit } from '@angular/core';
import { InitStateFormComponent } from '../init-state-form/init-state-form.component';
import { InitState } from '../../../../../../shared/model/api/init-state';
import { FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';
import { User } from '../../../../../../shared/model/api/user';
import { CurrentUserService } from '../../../../../services/current-user.service';
import { AuthenticationService } from '../../../../services/authentication.service';
import { InitService } from '../../../../../services/init.service';
import { PostAzureAdminUserData } from '../../../../../../shared/model/api/post-azure-admin-user-data';
import { FetchingService } from '../../../../../services/fetching.service';

@Component({
  selector: 'app-set-azure-admin-init-state-form',
  templateUrl: './set-azure-admin-init-state-form.component.html',
  styleUrls: ['./set-azure-admin-init-state-form.component.scss'],
})
export class SetAzureAdminInitStateFormComponent extends InitStateFormComponent implements OnInit {

  eventEmitter: EventEmitter<InitState> = new EventEmitter<InitState>();
  form: FormGroup;

  buttonsEnabled: boolean = true;
  currentUser$: Observable<User>;

  constructor(
    private currentUserService: CurrentUserService,
    private authenticationService: AuthenticationService,
    private initService: InitService,
    private fetchingService: FetchingService,
  ) {
    super();
  }

  ngOnInit(): void {
    this.currentUser$ = this.currentUserService.getCurrentUser$();
  }

  handleSubmitClick(user: User): void {
    const data: PostAzureAdminUserData = { id: user.id };
    this.buttonsEnabled = false;

    this.initService.postAzureAdminUser$(data)
      .subscribe((initState: InitState) => {
        this.buttonsEnabled = true;
        this.fetchingService.refetchSingle(CurrentUserService);
        this.eventEmitter.emit(initState);
      }, () => {
        this.buttonsEnabled = true;
      });
  }

  handleLogoutClick(): void {
    this.authenticationService.logout();
  }
}
