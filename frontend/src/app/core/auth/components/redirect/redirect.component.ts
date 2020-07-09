import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-redirect',
  templateUrl: './redirect.component.html',
  styleUrls: ['./redirect.component.scss']
})
export class RedirectComponent implements OnInit {

  constructor(private router: Router) {
  }

  ngOnInit(): void {
      setTimeout(() => {
        this.router.navigate(['/landingpage']);
      }, 100);  // setTimeout is needed to navigate in non chromium based browsers /TG 09.03.2020
  }
}
