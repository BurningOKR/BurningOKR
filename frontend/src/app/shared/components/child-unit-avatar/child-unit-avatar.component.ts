import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { OkrChildUnit } from '../../model/ui/OrganizationalUnit/okr-child-unit';

@Component({
  selector: 'app-child-unit-avatar',
  templateUrl: './child-unit-avatar.component.html',
  styleUrls: ['./child-unit-avatar.component.scss'],
})
export class ChildUnitAvatarComponent implements OnChanges {

  @Input() okrChildUnit: OkrChildUnit;
  @Input() size = 64;
  @Input() bgColor = '';

  render: boolean = true;

  ngOnChanges(changes: SimpleChanges): void {
    setTimeout(() => this.render = false);
    setTimeout(() => this.render = true);
  }

  buildInitialsForUserAvatar(okrChildUnit: OkrChildUnit): string {
    const nameSplit: string[] = okrChildUnit.name.split(' ', 3);

    if (nameSplit.length >= 2) {
      return `${nameSplit[0].charAt(0)} ${nameSplit[1].charAt(0)}`;
    } else if (nameSplit.length === 1) {
      if (nameSplit[0].length >= 1) {
        return `${nameSplit[0].charAt(0)} ${nameSplit[0].charAt(1)}`;
      } else {
        return `${nameSplit[0].charAt(0)}`;
      }

    } else {
      return null;
    }
  }

}
