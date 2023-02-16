import { OkrUnit } from './okr-unit';

export interface Structure extends OkrUnit {
  substructures: Structure[];
}
