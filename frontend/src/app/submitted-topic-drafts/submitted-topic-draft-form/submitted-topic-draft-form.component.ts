import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material';
import {SubmittedTopicDraftDetailsComponent} from "../submitted-topic-draft-details/submitted-topic-draft-details.component";

@Component({
  selector: 'app-submitted-topic-draft-form',
  templateUrl: './submitted-topic-draft-form.component.html',
  styleUrls: ['./submitted-topic-draft-form.component.css']
})
export class SubmittedTopicDraftFormComponent {

  constructor(private dialog: MatDialog) {
  }
}
