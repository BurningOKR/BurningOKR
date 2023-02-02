import { Component, Input } from '@angular/core';
import { PickLanguageComponent } from '../../../core/settings/pick-language/pick-language.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-okr-toolbar-bare',
  templateUrl: './okr-toolbar-bare.component.html',
  styleUrls: ['./okr-toolbar-bare.component.scss'],
})
export class OkrToolbarBareComponent {

  @Input() fixedPosition: boolean = false;

  constructor(
    private dialog: MatDialog,
  ) {
  }

  pickLanguageClicked() {
    this.dialog.open(PickLanguageComponent)
      .afterClosed()
      .subscribe();
  }
}
