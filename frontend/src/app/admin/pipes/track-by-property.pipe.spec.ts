import { TrackByPropertyPipe } from './track-by-property.pipe';

describe('TrackByPropertyPipe', () => {
  it('create an instance', () => {
    const pipe: TrackByPropertyPipe = new TrackByPropertyPipe();
    expect(pipe)
      .toBeTruthy();
  });
});
