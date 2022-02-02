import { Component, Input } from '@angular/core';
import { Structure } from '../../../shared/model/ui/OrganizationalUnit/structure';

@Component({
  selector: 'app-convert-topic-draft-tree',
  templateUrl: './convert-topic-draft-tree.component.html',
  styleUrls: ['./convert-topic-draft-tree.component.scss'],
})
export class ConvertTopicDraftTreeComponent {
  @Input() substructure: Structure;
  isOpen = false;

  hasChildUnits(): boolean {
    return this.substructure.substructures.length > 0;
  }

  toggleOpen(): void {
    this.isOpen = !this.isOpen;
  }

  test(): void {
    console.log('clicked');
    console.log(this.substructure);
  }
}
