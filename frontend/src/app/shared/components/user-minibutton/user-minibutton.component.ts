import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { User } from '../../model/api/user';
import { UserService } from '../../services/helper/user.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-user-minibutton',
  templateUrl: './user-minibutton.component.html',
  styleUrls: ['./user-minibutton.component.scss']
})
export class UserMinibuttonComponent implements OnInit, OnChanges {
  @Input() userId: string;
  @Input() canBeRemoved: boolean = false;

  @Output() userDeleted: EventEmitter<string> = new EventEmitter();

  user$: Observable<User>;

  constructor(
    private userService: UserService) {
  }

  ngOnInit(): void {
    this.getUser();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (!!changes.userId) {
      if (!changes.userId.firstChange) {
        this.getUser();
      }
    }
  }

  clickedDeleteButton(): void {
    this.userDeleted.emit(this.userId);
  }

  private getUser(): void {
    this.user$ = this.userService.getUserById$(this.userId);
  }
}
