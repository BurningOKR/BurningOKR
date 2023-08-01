import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-child-unit-avatar',
  templateUrl: './child-unit-avatar.component.html',
  styleUrls: ['./child-unit-avatar.component.scss'],
})
export class ChildUnitAvatarComponent implements OnChanges {

  @Input() name: string;
  @Input() size = 64;
  @Input() bgColor = '';
  @Input() clickable = false;
  @Output() clickOnAvatar: EventEmitter<any> = new EventEmitter<any>();

  render: boolean = true;

  ngOnChanges(changes: SimpleChanges): void {
    setTimeout(() => this.render = false);
    setTimeout(() => this.render = true);
  }

  buildInitialsForUserAvatar(name: string): string {
    const nameSplit: string[] = name.split(' ', 3);

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
