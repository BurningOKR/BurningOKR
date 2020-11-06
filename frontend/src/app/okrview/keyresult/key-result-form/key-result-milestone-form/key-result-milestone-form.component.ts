import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { AbstractControl, FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { ViewKeyResult } from '../../../../shared/model/ui/view-key-result';
import { ViewKeyResultMilestone } from '../../../../shared/model/ui/view-key-result-milestone';

@Component({
  selector: 'app-key-result-milestone-form',
  templateUrl: './key-result-milestone-form.component.html',
  styleUrls: ['./key-result-milestone-form.component.scss']
})
export class KeyResultMilestoneFormComponent implements OnInit, OnChanges {

  isChecked: boolean = false;

  @Input() keyResult: ViewKeyResult;
  @Input() formArray: FormArray;
  @Input() start: number;
  @Input() end: number;

  private min: number;
  private max: number;

  ngOnInit(): void {
    this.updateMinMaxValues();
    if (this.keyResult) {
      this.isChecked = this.keyResult.viewKeyResultMilestones.length > 0;
      this.keyResult.viewKeyResultMilestones.forEach((viewKeyResultMilestone: ViewKeyResultMilestone) => {
        this.addMilestone(
          viewKeyResultMilestone.id,
          viewKeyResultMilestone.name,
          viewKeyResultMilestone.value,
          viewKeyResultMilestone.parentKeyResult
        );
      });
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.start || changes.end) {
      this.updateMinMaxValidators();
    }
  }

  addMilestone(id: number, name: string, value: number, parentKeyResultId: number): void {
    this.formArray.push(new FormGroup({
      id: new FormControl(id),
      name: new FormControl(name, [Validators.required, Validators.maxLength(255)]),
      value: new FormControl(value, [Validators.required, Validators.min(this.min), Validators.max(this.max)]),
      parentKeyResult: new FormControl(parentKeyResultId)
    }));
  }

  deleteMilestone(milestoneForm: AbstractControl): void {
    const index: number = this.formArray.controls.findIndex(control => control === milestoneForm);
    if (index >= 0) {
      this.formArray.removeAt(index);
    }
  }

  private updateMinMaxValues(): void {
    if (this.start <= this.end) {
      this.min = this.start;
      this.max = this.end;
    } else {
      this.min = this.end;
      this.max = this.start;
    }
  }

  private updateMinMaxValidators(): void {
    this.updateMinMaxValues();
    this.formArray.controls.forEach((control: AbstractControl) => {
      const valueControl: AbstractControl = control.get('value');
      valueControl.clearValidators();
      valueControl.setValidators([Validators.required, Validators.min(this.min), Validators.max(this.max)]);
      valueControl.markAsTouched();
      valueControl.updateValueAndValidity({ emitEvent: true });
      this.formArray.updateValueAndValidity();
    });
  }
}
