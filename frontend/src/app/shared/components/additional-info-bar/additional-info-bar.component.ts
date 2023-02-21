import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-additional-info-bar',
  templateUrl: './additional-info-bar.component.html',
  styleUrls: ['./additional-info-bar.component.scss']
})
export class AdditionalInfoBarComponent implements OnInit {

  @Input() text: string;
  constructor() { }

  ngOnInit(): void {
  }

}
