import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-additional-info-bar',
  templateUrl: './additional-info-bar.component.html',
  styleUrls: ['./additional-info-bar.component.scss'],
})
export class AdditionalInfoBarComponent {
  @Input() text: string;
}
