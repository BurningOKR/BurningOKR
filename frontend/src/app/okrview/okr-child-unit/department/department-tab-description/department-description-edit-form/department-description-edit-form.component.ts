import { Component, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { User } from '../../../../../shared/model/api/user';
import { UserService } from '../../../../../shared/services/helper/user.service';

@Component({
  selector: 'app-department-description-edit-form',
  templateUrl: './department-description-edit-form.component.html',
  styleUrls: ['./department-description-edit-form.component.css']
})
export class DepartmentDescriptionEditFormComponent implements OnInit {
  descriptionForm: FormGroup;
  title: string;
  users$: Observable<User[]>;
  user: User;
  startteam: User[] = [];

  constructor(
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.descriptionForm = new FormGroup({
      topic: new FormControl('', Validators.maxLength(255)),
      acceptanceCriteria: new FormControl('', Validators.maxLength(255)),
      contributesTo: new FormControl('', Validators.maxLength(255)),
      delimitation: new FormControl('', Validators.maxLength(255)),
      beginning: new FormControl(),
      dependencies: new FormControl('', Validators.maxLength(255)),
      resources: new FormControl('', Validators.maxLength(255)),
      handoverPlan: new FormControl('', Validators.maxLength(255)),
      initiator: new FormControl(),
      startteam: new FormArray([new FormControl()]),
      stakeholder: new FormArray([])
      });

    this.users$ = this.userService.getAllUsers$();
  }

  temp(): void {  }

  onSelectInitiator($event: { value: User; }): void {
    this.user = $event.value;
  }

  onSelectStartteamMember($event: {value: User; }, member: FormControl): void {
    const formArray: FormArray = this.getFormArray();
    if (!$event.value) {
      const position: number = formArray.controls.indexOf(member);
      if(formArray.controls.length > 1) {
        formArray.removeAt(position);
      }
    } else {
      this.startteam.Add($event.value);
      formArray.push(new FormControl());
    }
  }

  getFormArray(): FormArray {
    return (this.descriptionForm.get('startteam') as FormArray);
  }
}
