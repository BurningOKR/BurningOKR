import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-non-logged-in-card',
  templateUrl: './non-logged-in-card.component.html',
  styleUrls: ['./non-logged-in-card.component.scss'],
})
export class NonLoggedInCardComponent {

  @Input() toolbarTitle: string;
}
