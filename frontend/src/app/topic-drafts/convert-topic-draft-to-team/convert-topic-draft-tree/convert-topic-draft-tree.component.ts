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

  selectStructure(): void {
    console.log('clicked');
    console.log(this.substructure);
    /*
    TODO: P.B. 04.02.2022
        - One way to implement is that this needs to be emitted up to the convert-submitted-topic-draft-to-team component as the selected structure where it will be created.
        - Another would be to split the create logic into its own service, remove the convert button and open a small dialog when clicking on a substructure.
          The dialog then asks the user if that is where he wants the team to be created.
        - I am also not sure if a tree-structure is what we want here. It becomes overcrowded when there are too many substructures (or structures have long names).
          Ok for BAG, probably not OK for bigger companies.
        !IMPORTANT: Mobile View doesnt work right now. The dialog is out of screen on iphone SE for example.
    */
  }
}
