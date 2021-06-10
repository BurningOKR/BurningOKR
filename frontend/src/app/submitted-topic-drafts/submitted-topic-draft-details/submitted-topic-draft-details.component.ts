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

  // @Input()
  topicDraft: OkrTopicDraft;
  enumStatus = status;

  constructor() {
    this.topicDraft = new OkrTopicDraft(
      1,
      status.rejected,
      new User("746eabe8-e36c-4b46-b72d-f24a249b3860", "Markus","Mueller","mmueller@web.de","IT-Consultant","IT","photo.jpg",true),
      2,
      'Automatische Generierung von Essen',
      'sddasdasadsdafs',
      ["746eabe8-e36c-4b46-b72d-f24a249b3860", "7c8db234-a6f2-4bff-9b95-dd26ef82246b", "b16b928b-6259-4f12-87f4-00e2f8fb6385"],
      ["d223f694-8a45-4ece-8a17-d265149ae260", "746eabe8-e36c-4b46-b72d-f24a249b3860"],
      'Irgendwelche Kriterien',
      'alles was läuft und fährt',
      'Irgendwas wird doch immer reguliert',
      new Date(2018, 11, 24, 10, 33, 30, 0),
      'Ganz ganz viele Dependencies',
      'Irgendwas mit Resourcen',
      'Ein ganz toller Plan zum Übergeben von Sachen und Essen'
    )
  }
}
