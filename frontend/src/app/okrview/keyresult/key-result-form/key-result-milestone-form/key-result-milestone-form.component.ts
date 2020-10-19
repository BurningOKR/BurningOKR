import { Component } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-key-result-milestone-form',
  templateUrl: './key-result-milestone-form.component.html',
  styleUrls: ['./key-result-milestone-form.component.scss']
})
export class KeyResultMilestoneFormComponent {

  isChecked: boolean = false;
  milestoneForm: FormArray = new FormArray([]);

  addMilestone(): void {
    this.milestoneForm.controls.push(new FormGroup({
      nameControl: new FormControl('', [Validators.required, Validators.maxLength(255)]),
      targetValueControl: new FormControl('', [Validators.required, Validators.min(1)]),
    }));
  }

  deleteMilestone(milestoneForm: FormGroup): void {
    const index: number = this.milestoneForm.controls.findIndex(control => control === milestoneForm);
    this.milestoneForm.removeAt(index);
  }
}
