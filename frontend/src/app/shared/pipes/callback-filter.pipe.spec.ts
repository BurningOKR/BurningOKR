import {CallbackFilterPipe} from './callback-filter.pipe';

describe('CallbackFilterPipe', () => {
  it('create an instance', () => {
    const pipe: CallbackFilterPipe = new CallbackFilterPipe();
    expect(pipe)
      .toBeTruthy();
  });
});
