import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { UserService } from '../../shared/services/helper/user.service';
import { User } from '../../shared/model/api/user';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-okr-topic-description-form',
  templateUrl: './okr-topic-description-form.component.html',
  styleUrls: ['./okr-topic-description-form.component.css']
})
export class OkrTopicDescriptionFormComponent implements OnInit {

  @Input() descriptionForm: FormGroup;
  users$: Observable<User[]>;
  minBeginn: Date;

  constructor(private userService: UserService) {
    this.minBeginn = new Date();
  }

  ngOnInit(): void {
    this.users$ = this.userService.getAllUsers$();
  }
}
