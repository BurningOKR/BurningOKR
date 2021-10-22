import { AfterViewInit, Component, ElementRef, HostListener, ViewChild } from '@angular/core';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-demo-footer',
  templateUrl: './demo-footer.component.html',
  styleUrls: ['./demo-footer.component.scss']
})
export class DemoFooterComponent {
  @ViewChild('footer', {static: true}) footer: ElementRef;

  isPlayground: boolean = environment.playground;
}
