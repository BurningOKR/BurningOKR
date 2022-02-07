import { LogPipe } from './log.pipe';

describe('LogPipe', () => {
  it('create an instance', () => {
    const pipe = new LogPipe();
    expect(pipe).toBeTruthy();
  });
});
