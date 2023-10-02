import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { User } from '../../../../../shared/model/api/user';
import { UserService } from '../../../../../shared/services/helper/user.service';
import { CurrentUserService } from '../../../../../core/services/current-user.service';
import { Observable, Subscription } from 'rxjs';
import { UserId } from '../../../../../shared/model/id-types';

@Component({
  selector: 'app-taskboard-users',
  templateUrl: './taskboard-users.component.html',
  styleUrls: ['./taskboard-users.component.scss'],
})
export class TaskboardUsersComponent implements OnInit, OnDestroy, OnChanges {

  @Input() users: UserId[];
  @Input() maxShownUsers: number = 5;
  @Input() avatarSize: number = 30;

  countAdditionalUsers: number;
  users$: Observable<User>[];

  private currentUserSubscription: Subscription;
  private currentUserId: string;

  constructor(private currentUserService: CurrentUserService, private userService: UserService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    const currentUsersLength: number = this.users?.length ?? 0;
    this.users$ = [];
    if (currentUsersLength <= this.maxShownUsers) {
      this.users?.forEach(userId => this.users$.push(this.userService.getUserById$(userId)));
      this.countAdditionalUsers = 0;
    } else {
      const currentShownUsers: UserId[] = this.users
        .filter(userId => userId !== this.currentUserId)
        .slice(0, this.maxShownUsers - 1);
      currentShownUsers.forEach(userId => this.users$.push(this.userService.getUserById$(userId)));
      this.countAdditionalUsers = currentUsersLength - currentShownUsers.length;
    }
  }

  ngOnDestroy(): void {
    this.currentUserSubscription.unsubscribe();
  }

  ngOnInit(): void {
    this.currentUserSubscription = this.currentUserService.getCurrentUserId$().subscribe(userId => this.currentUserId = userId);
  }

}

