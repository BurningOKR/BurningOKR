import { Component, Input } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { User } from '../../shared/model/api/user';

@Component({
  selector: 'app-submitted-topic-draft-details',
  templateUrl: './submitted-topic-draft-details.component.html',
  styleUrls: ['./submitted-topic-draft-details.component.css']
})
export class SubmittedTopicDraftDetailsComponent {

  @Input() topicDraft: OkrTopicDraft;
  enumStatus = status;

  constructor() {
    this.topicDraft = new OkrTopicDraft(
      1,
      status.submitted,
      new User('575eb66a-75bf-4c5a-88ab-b56ebe49fd42', 'Spongebob', 'Schwammkopf', 'bikiniBottom@mail.de', 'Burgerbratter', '', '', true),
      2,
      'Automatische Generierung von Essen',
      'sddasdasadsdafs',
      ['575eb66a-75bf-4c5a-88ab-b56ebe49fd42', 'ccc5c00f-04e6-4a23-b157-781163841bb5'],
      ['575eb66a-75bf-4c5a-88ab-b56ebe49fd42', 'ccc5c00f-04e6-4a23-b157-781163841bb5'],
      'Irgendwelche Kriterien',
      'alles was läuft und fährt',
      'Irgendwas wird doch immer reguliert',
      new Date(2018, 11, 24, 10, 33, 30, 10),
      'Ganz ganz viele Dependencies',
      'Irgendwas mit Ressourcen',
      'Ein ganz toller Plan zum Übergeben von Sachen und Essen'
    );
  }
}
