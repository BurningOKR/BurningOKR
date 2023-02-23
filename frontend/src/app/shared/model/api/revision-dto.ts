import {UserId} from '../id-types';

export interface RevisionDto {
  date: number;
  user: UserId;
  revisionType: RevisionType;
  revisionValueType: RevisionValueType;
  changedField: string;
  oldValue: any;
  newValue: any;
}
type RevisionType = 'ADD' | 'MOD' | 'DEL';
type RevisionValueType = 'STRING' | 'USER_COLLECTION';
