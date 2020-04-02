import { CycleTransferService } from './cycle-transfer.service';
import { CycleState, CycleUnit } from '../../shared/model/ui/cycle-unit';

describe('CycleTransferService', () => {
  let service: CycleTransferService;

  beforeEach(() => {
    service = new CycleTransferService();
  });

  it('next value of transferredCycle$ should contain cycle which was previously exported', () => {
    const expected: CycleUnit = new CycleUnit(42, 'magic', [2], new Date(3), new Date(4), CycleState.ACTIVE, true);

    service.exportCycle(expected);

    service.transferredCycle$.subscribe((actual: CycleUnit) => {
      expect(actual)
        .toEqual(expected);
    });
  });

  it('next value of transferredCycle$ should contain cycle which was previously exported lastly', () => {
    const expected: CycleUnit = new CycleUnit(42, 'magic', [2], new Date(3), new Date(4), CycleState.ACTIVE, true);
    const notExpected: CycleUnit = new CycleUnit(3, 'oof', [1], new Date(2), new Date(3), CycleState.CLOSED, true);

    service.exportCycle(notExpected);
    service.exportCycle(expected);

    service.transferredCycle$.subscribe((actual: CycleUnit) => {
      expect(actual)
        .toEqual(expected);
    });
  });
});
