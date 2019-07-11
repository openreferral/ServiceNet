export interface IServiceAtLocation {
  id?: number;
  description?: any;
  srvcName?: string;
  srvcId?: number;
  locationName?: string;
  locationId?: number;
  externalDbId?: string;
  providerName?: string;
}

export const defaultValue: Readonly<IServiceAtLocation> = {};
