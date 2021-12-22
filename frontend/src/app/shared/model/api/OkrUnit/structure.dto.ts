import { Collection } from "typescript";
import {CompanyDto} from "./company.dto";
import {OkrUnitSchema} from "../../ui/okr-unit-schema";

export interface StructureDto extends CompanyDto {

  unitSchema: OkrUnitSchema[];
}
