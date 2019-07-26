import { IOpeningHours } from 'app/shared/model//opening-hours.model';

export interface IRegularSchedule {
  id?: number;
  srvcName?: string;
  srvcId?: number;
  locationName?: string;
  locationId?: number;
  openingHours?: IOpeningHours[];
  notes?: string;
}

export const defaultValue: Readonly<IRegularSchedule> = {};
