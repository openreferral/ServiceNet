import { IPhone } from 'app/shared/model//phone.model';

export interface IServiceAtLocation {
  id?: number;
  description?: any;
  srvcName?: string;
  srvcId?: number;
  locationName?: string;
  locationId?: number;
  regularScheduleId?: number;
  holidayScheduleId?: number;
  phones?: IPhone[];
  externalDbId?: string;
  providerName?: string;
}

export const defaultValue: Readonly<IServiceAtLocation> = {};
