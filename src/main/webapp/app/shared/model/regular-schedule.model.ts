export interface IRegularSchedule {
  id?: number;
  weekday?: number;
  opensAt?: string;
  closesAt?: string;
  srvcName?: string;
  srvcId?: number;
  locationName?: string;
  locationId?: number;
  serviceAtlocationId?: number;
}

export const defaultValue: Readonly<IRegularSchedule> = {};
