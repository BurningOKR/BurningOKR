import { AdminUserIdsPipe } from './admin-user-ids.pipe';

describe('AdminUserIdsPipe', () => {
  it('create an instance', () => {
    const pipe: AdminUserIdsPipe = new AdminUserIdsPipe();
    expect(pipe)
      .toBeTruthy();
  });
});
