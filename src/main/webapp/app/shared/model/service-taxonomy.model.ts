export interface IServiceTaxonomy {
  id?: number;
  taxonomyDetails?: any;
  srvcName?: string;
  srvcId?: number;
  taxonomyName?: string;
  taxonomyId?: number;
  externalDbId?: string;
  providerName?: string;
}

export const defaultValue: Readonly<IServiceTaxonomy> = {};
