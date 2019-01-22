export interface IContact {
  id?: number;
  name?: string;
  title?: string;
  department?: string;
  email?: string;
  organizationName?: string;
  organizationId?: number;
  srvcName?: string;
  srvcId?: number;
  serviceAtLocationId?: number;
  externalDbId?: string;
  providerName?: string;
}

export const defaultValue: Readonly<IContact> = {};
