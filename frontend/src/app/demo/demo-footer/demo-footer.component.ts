import { Component, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-demo-footer',
  templateUrl: './demo-footer.component.html',
  styleUrls: ['./demo-footer.component.scss'],
})
export class DemoFooterComponent {

  @ViewChild('footer', { static: true }) footer: ElementRef;
}
