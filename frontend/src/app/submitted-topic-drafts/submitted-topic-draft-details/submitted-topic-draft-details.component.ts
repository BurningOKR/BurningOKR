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

  /*constructor() {
    this.topicDraft = new OkrTopicDraft(
      1,
      status.rejected,
      new User("1001", "Markus","Mueller","mmueller@web.de","IT-Consultant","IT","photo.jpg",true),
      2,
      'Automatische Generierung von Essen',
      'sddasdasadsdafs',
      ["sdsd", "sdds", "bhg"],
      ["jh", "fdsfd"],
      'Irgendwelche Kriterien',
      'alles was läuft und fährt',
      'Irgendwas wird doch immer reguliert',
      new Date(2018, 11, 24, 10, 33, 30, 0),
      'Ganz ganz viele Dependencies',
      'Irgendwas mit Resourcen',
      'Ein ganz toller Plan zum Übergeben von Sachen und Essen'
    )
  }*/
}
