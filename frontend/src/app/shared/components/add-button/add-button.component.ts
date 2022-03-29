import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-add-button',
  templateUrl: './add-button.component.html',
  styleUrls: ['./add-button.component.scss'],
})
export class AddButtonComponent implements OnInit {

  constructor() {
  }

  @Input() text: String;
  @Input() isDisabled: boolean;
  @Input() matIcon: String = "add_circle_outline";
  @Output() buttonClicked: EventEmitter<String> = new EventEmitter<String>()

  ngOnInit(): void {
  }

  clickButton() {
    this.buttonClicked.emit();
  }
}
