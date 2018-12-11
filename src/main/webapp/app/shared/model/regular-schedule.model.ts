import { IOpeningHours } from 'app/shared/model//opening-hours.model';

export interface IRegularSchedule {
  id?: number;
  srvcName?: string;
  srvcId?: number;
  locationName?: string;
  locationId?: number;
  serviceAtlocationId?: number;
  openingHours?: IOpeningHours[];
}

export const defaultValue: Readonly<IRegularSchedule> = {};
