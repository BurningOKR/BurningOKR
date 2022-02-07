import { LogPipe } from './log.pipe';

describe('LogPipe', () => {
  it('create an instance', () => {
    const pipe: LogPipe = new LogPipe();
    expect(pipe).toBeTruthy();
  });
});
