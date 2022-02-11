import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TopicDraftsComponent } from './topic-drafts-component/topic-drafts.component';
import { TopicDraftCardComponent } from './topic-draft-card-component/topic-draft-card.component';
import {
  TopicDraftActionButtonComponent
} from './topic-draft-action-button-component/topic-draft-action-button.component';
import { TopicDraftsListComponent } from './topic-drafts-list-component/topic-drafts-list.component';
import {
  TopicDraftDetailsDialogueComponent
} from './topic-draft-details-dialogue-component/topic-draft-details-dialogue.component';
import {
  TopicDraftEditDialogueComponent
} from './topic-draft-edit-dialogue-component/topic-draft-edit-dialogue.component';
import {
  ConvertTopicDraftToTeamDialogueComponent
} from './convert-topic-draft-to-team/convert-topic-draft-to-team-dialogue-component/convert-topic-draft-to-team-dialogue.component';
import {
  ConvertTopicDraftTreeComponent
} from './convert-topic-draft-to-team/convert-topic-draft-tree/convert-topic-draft-tree.component';
import { SharedModule } from '../shared/shared.module';
import { MatIconModule } from '@angular/material/icon';
import { TranslateModule } from '@ngx-translate/core';
import { AppRoutingModule } from '../app-routing.module';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatOptionModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatMenuModule } from '@angular/material/menu';
import { OkrviewModule } from '../okrview/okrview.module';
import { MatDialogModule } from '@angular/material/dialog';
import { MatExpansionModule } from '@angular/material/expansion';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';

@NgModule({
  declarations: [
    TopicDraftsComponent,
    TopicDraftCardComponent,
    TopicDraftActionButtonComponent,
    TopicDraftsListComponent,
    TopicDraftDetailsDialogueComponent,
    TopicDraftEditDialogueComponent,
    ConvertTopicDraftToTeamDialogueComponent,
    ConvertTopicDraftTreeComponent,
  ],
  imports: [
    CommonModule,
    SharedModule,
    MatIconModule,
    MatTooltipModule,
    MatOptionModule,
    MatFormFieldModule,
    MatSelectModule,
    MatMenuModule,
    TranslateModule,
    OkrviewModule,
    MatDialogModule,
    MatExpansionModule,
    AppRoutingModule,
    ReactiveFormsModule,
    MatCardModule,
    MatInputModule,
  ],
  exports: [TopicDraftsComponent]
})
export class TopicDraftsModule { }
