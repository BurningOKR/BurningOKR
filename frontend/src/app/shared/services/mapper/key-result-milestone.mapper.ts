import { Injectable } from '@angular/core';
import { ViewKeyResultMilestone } from '../../model/ui/view-key-result-milestone';
import { KeyResultMilestoneDto } from '../../model/api/key-result-milestone.dto';

@Injectable({
  providedIn: 'root',
})
export class KeyResultMilestoneMapper {

  static mapToKeyResultMilestoneDto(viewKeyResultMilestone: ViewKeyResultMilestone): KeyResultMilestoneDto {
    return {
      id: viewKeyResultMilestone.id,
      name: viewKeyResultMilestone.name,
      parentKeyResultId: viewKeyResultMilestone.parentKeyResult,
      value: viewKeyResultMilestone.value,
    };
  }

  static mapToKeyResultMilestoneDtos(viewKeyResultMilestones: ViewKeyResultMilestone[]): KeyResultMilestoneDto[] {
    return viewKeyResultMilestones.map((viewKeyResultMilestone: ViewKeyResultMilestone) => {
      return KeyResultMilestoneMapper.mapToKeyResultMilestoneDto(viewKeyResultMilestone);
    });
  }

  static mapToViewKeyResultMilestone(keyResultMilestoneDto: KeyResultMilestoneDto): ViewKeyResultMilestone {
    return new ViewKeyResultMilestone(
      keyResultMilestoneDto.id,
      keyResultMilestoneDto.name,
      keyResultMilestoneDto.parentKeyResultId,
      keyResultMilestoneDto.value,
    );
  }

  static mapToViewkeyResultMilestones(keyResultMilestones: KeyResultMilestoneDto[]): ViewKeyResultMilestone[] {
    return keyResultMilestones.map((keyResultMilestoneDto: ViewKeyResultMilestone) => {
      return KeyResultMilestoneMapper.mapToViewKeyResultMilestone(keyResultMilestoneDto);
    });
  }
}
