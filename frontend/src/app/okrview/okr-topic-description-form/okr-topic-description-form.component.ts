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
  @Input() minBeginn: Date = new Date();
  users$: Observable<User[]>;

  constructor(private userService: UserService) {
  }

  ngOnInit(): void {
    this.users$ = this.userService.getAllUsers$();
    if(this.descriptionForm?.get('beginning')?.value !== undefined  &&
        this.minBeginn.getTime() > this.descriptionForm?.get('beginning')?.value?.getTime()){
      this.minBeginn = this.descriptionForm.get('beginning').value;
    }
  }
}
