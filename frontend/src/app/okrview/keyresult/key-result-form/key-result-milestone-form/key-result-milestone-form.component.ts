import { Component, OnInit } from '@angular/core';
import {FormArray, FormControl, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-key-result-milestone-form',
  templateUrl: './key-result-milestone-form.component.html',
  styleUrls: ['./key-result-milestone-form.component.scss']
})
export class KeyResultMilestoneFormComponent implements OnInit {

  isChecked: boolean = false;
  milestoneForm: FormArray = new FormArray([]);

  addMilestone() {
    this.milestoneForm.controls.push(new FormGroup({
      nameControl: new FormControl(),
      targetValueControl: new FormControl(),
    }));
  }

  deleteMilestone(milestoneForm: FormGroup) {
    const index: number = this.milestoneForm.controls.findIndex(control => control === milestoneForm);
    this.milestoneForm.removeAt(index);
  }

  constructor() { }

  ngOnInit() {
  }
}
