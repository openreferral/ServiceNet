export interface IServiceTaxonomy {
  id?: number;
  taxonomyDetails?: any;
  srvcName?: string;
  srvcId?: number;
  taxonomyName?: string;
  taxonomyId?: number;
}

export const defaultValue: Readonly<IServiceTaxonomy> = {};
