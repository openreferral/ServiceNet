export interface IPhone {
  id?: number;
  number?: string;
  extension?: number;
  type?: string;
  language?: string;
  description?: any;
  locationName?: string;
  locationId?: number;
  srvcName?: string;
  srvcId?: number;
  organizationName?: string;
  organizationId?: number;
  contactName?: string;
  contactId?: number;
  serviceAtLocationId?: number;
}

export const defaultValue: Readonly<IPhone> = {};
