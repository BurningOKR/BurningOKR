import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { User } from '../../model/api/user';
import { UserMapper } from '../../services/mapper/user.mapper';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-user-minibutton',
  templateUrl: './user-minibutton.component.html',
  styleUrls: ['./user-minibutton.component.scss']
})
export class UserMinibuttonComponent implements OnInit {
  @Input() userId: string;
  @Input() canBeRemoved: boolean = false;

  @Output() userDeleted: EventEmitter<string> = new EventEmitter();

  user$: Observable<User>;

  constructor(
    private userMapperService: UserMapper) {
  }

  ngOnInit(): void {
    this.user$ = this.userMapperService.getUserById$(this.userId);
  }

  clickedDeleteButton(): void {
    this.userDeleted.emit(this.userId);
  }
}
