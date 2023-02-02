import { OkrUnitDto } from './okr-unit.dto';

export interface StructureDto extends OkrUnitDto {
  substructure: StructureDto[];
}
