import { ChartCreationOptionsDto } from './chart-creation-options.dto';
import { CompanyId, DashboardId, UserId } from '../../../shared/model/id-types';

/**
 * Used only for newly created Dashboards delivered to the Backend for the first time.
 * DashboardId and date of creation are not set in the Frontend but in the Backend,
 * therefore this Dto differs to the standard Dto for Dashboards.
 * It still allows an optional ID
 * because currently the Backend returns a DashboardCreationDto
 * after the same one was saved in the Database but with ID.
 */
export interface DashboardCreationDto {
  id?: DashboardId;
  title: string;
  creatorId?: UserId;
  companyId: CompanyId;
  chartCreationOptions: ChartCreationOptionsDto[];
}
