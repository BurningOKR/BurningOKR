export class ContactData {
  contactPersonName: string;
  contactPersonMail: string;

  constructor(name: string = '', mail: string = '') {
    this.contactPersonName = name;
    this.contactPersonMail = mail;
  }

}
