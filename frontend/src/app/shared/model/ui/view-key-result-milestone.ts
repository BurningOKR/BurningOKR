export class ViewKeyResultMilestone {
  id?: number;
  name: string;
  parentKeyResult?: number;
  value: number;

  constructor(id: number, name: string, parentKeyResult: number, value: number) {
    this.id = id;
    this.name = name;
    this.parentKeyResult = parentKeyResult;
    this.value = value;
  }

}
