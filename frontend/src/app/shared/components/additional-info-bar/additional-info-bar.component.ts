import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-additional-info-bar',
  templateUrl: './additional-info-bar.component.html',
  styleUrls: ['./additional-info-bar.component.scss']
})
export class AdditionalInfoBarComponent implements OnInit {

  @Input() text: string;
  // eslint-disable-next-line @typescript-eslint/no-empty-function
  constructor() { }

  // eslint-disable-next-line @typescript-eslint/no-empty-function
  ngOnInit(): void {
  }
}
