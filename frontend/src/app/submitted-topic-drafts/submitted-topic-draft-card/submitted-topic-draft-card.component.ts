import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-submitted-topic-draft-card',
  templateUrl: './submitted-topic-draft-card.component.html',
  styleUrls: ['./submitted-topic-draft-card.component.css']
})
export class SubmittedTopicDraftCardComponent implements OnInit {

  private isFolded: boolean = false;
  private foldIcon: string = 'arrow_drop_down';

  ngOnInit(): void {
    // to be done
  }

  foldButtonClicked(): void {
    this.isFolded = !this.isFolded;
    this.isFolded ? this.foldIcon = 'arrow_drop_up' : this.foldIcon = 'arrow_drop_down';
  }
}
