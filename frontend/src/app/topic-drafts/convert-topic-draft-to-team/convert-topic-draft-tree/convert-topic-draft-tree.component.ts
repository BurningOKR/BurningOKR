import { Component, Input, OnInit } from '@angular/core';
import { Structure } from '../../../shared/model/ui/OrganizationalUnit/structure';
import { Observable } from 'rxjs';
import { ConvertTopicDraftToTeamService } from '../convert-topic-draft-to-team.service';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-convert-topic-draft-tree',
  templateUrl: './convert-topic-draft-tree.component.html',
  styleUrls: ['./convert-topic-draft-tree.component.scss'],
})
export class ConvertTopicDraftTreeComponent implements OnInit {
  @Input() substructure: Structure;
  isSelected: Observable<boolean>;
  isOpen = false;

  constructor(private convertTopicDraftToTeamService: ConvertTopicDraftToTeamService) { }

  ngOnInit(): void {
    this.isSelected = this.convertTopicDraftToTeamService.getSelectedUnit$()
      .pipe(map(currentSubstructure => currentSubstructure === this.substructure));
  }

  hasChildUnits(): boolean {
    return this.substructure.substructures.length > 0;
  }

  toggleOpen(): void {
    this.isOpen = !this.isOpen;
  }

  selectStructure(): void {
    this.convertTopicDraftToTeamService.setSelectedUnit(this.substructure);
  }
}
