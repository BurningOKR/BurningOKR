import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { ViewKeyResult } from '../../../../shared/model/ui/view-key-result';
import { ViewKeyResultMilestone } from '../../../../shared/model/ui/view-key-result-milestone';

@Component({
  selector: 'app-key-result-milestone-form',
  templateUrl: './key-result-milestone-form.component.html',
  styleUrls: ['./key-result-milestone-form.component.scss']
})
export class KeyResultMilestoneFormComponent implements OnInit {

  @Input() keyResult: ViewKeyResult;
  @Input() formArray: FormArray;

  isChecked: boolean = false;

  ngOnInit(): void {
    if (this.keyResult) {
      this.keyResult.viewKeyResultMilestones.forEach((viewKeyResultMilestone: ViewKeyResultMilestone) => {
        this.addMilestone(viewKeyResultMilestone.name, viewKeyResultMilestone.value);
      });
    }
  }

  addMilestone(name: string, value: number): void {
    this.formArray.push(new FormGroup({
      nameControl: new FormControl(name, [Validators.required, Validators.maxLength(255)]),
      targetValueControl: new FormControl(value, [Validators.required, Validators.min(1)]),
    }));
  }

  deleteMilestone(milestoneForm: FormGroup): void {
    const index: number = this.formArray.controls.findIndex(control => control === milestoneForm);
    this.formArray.removeAt(index);
  }
}
