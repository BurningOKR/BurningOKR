import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-okr-toolbar-bare',
  templateUrl: './okr-toolbar-bare.component.html',
  styleUrls: ['./okr-toolbar-bare.component.scss']
})
export class OkrToolbarBareComponent {

  @Input() fixedPosition: boolean = false;
}
