import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-no-mail-information',
  templateUrl: './no-mail-information.component.html',
  styleUrls: ['./no-mail-information.component.css'],
})
export class NoMailInformationComponent {

  constructor(private router: Router) {
  }

  navigateToCompanies(): void {
    this.router.navigate(['companies'])
      .catch();
  }
}
