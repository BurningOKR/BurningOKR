import { UserId } from '../id-types';

export class User {
  id: UserId;
  givenName: string;
  surname: string;
  jobTitle: string;
  department: string;
  email: string;
  photo: string;
  active: boolean;
  admin: boolean;

  constructor(
    id: UserId = '',
    givenName: string = '',
    surname: string = '',
    email: string = '',
    jobTitle: string = '',
    department: string = '',
    photo: string = '',
    active: boolean = true,
    admin: boolean = false,
  ) {
    this.id = id;
    this.givenName = givenName;
    this.surname = surname;
    this.jobTitle = jobTitle;
    this.department = department;
    this.email = email.toLowerCase();
    this.photo = photo;
    this.active = active;
    this.admin = admin;
  }
}
