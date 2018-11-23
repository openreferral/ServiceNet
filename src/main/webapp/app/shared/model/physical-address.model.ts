export interface IPhysicalAddress {
  id?: number;
  attention?: string;
  address1?: string;
  city?: string;
  region?: string;
  stateProvince?: string;
  postalCode?: string;
  country?: string;
  locationName?: string;
  locationId?: number;
}

export const defaultValue: Readonly<IPhysicalAddress> = {};
