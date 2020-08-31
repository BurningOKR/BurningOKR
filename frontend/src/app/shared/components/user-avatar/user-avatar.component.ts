import { Component, EventEmitter, Input, Output } from '@angular/core';
import { User } from '../../model/api/user';

@Component({
  selector: 'app-user-avatar',
  templateUrl: './user-avatar.component.html',
  styleUrls: ['./user-avatar.component.css']
})
export class UserAvatarComponent {
  @Input() user: User;
  @Input() size = 64;
  @Input() bgColor = '';
  @Input() clickable = false;
  @Output() clickOnAvatar: EventEmitter<any> = new EventEmitter<any>();
}
