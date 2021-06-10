import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-multiline-text-field',
  templateUrl: './multiline-text-field.component.html',
  styleUrls: ['./multiline-text-field.component.css']
})
export class MultilineTextFieldComponent {

  @Input() heading: number;
  @Input() content: string;
}
