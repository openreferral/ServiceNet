import { Moment } from 'moment';

export interface IHolidaySchedule {
  id?: number;
  closed?: boolean;
  opensAt?: string;
  closesAt?: string;
  startDate?: Moment;
  endDate?: Moment;
  srvcName?: string;
  srvcId?: number;
  locationName?: string;
  locationId?: number;
  serviceAtlocationId?: number;
}

export const defaultValue: Readonly<IHolidaySchedule> = {
  closed: false
};
