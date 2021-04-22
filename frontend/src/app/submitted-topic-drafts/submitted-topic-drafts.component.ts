import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-submitted-topic-drafts',
  templateUrl: './submitted-topic-drafts.component.html',
  styleUrls: ['./submitted-topic-drafts.component.css']
})
export class SubmittedTopicDraftsComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }

  navigateToCompanies(): void {
    this.router.navigate(['companies'])
        .catch();
  }
}
