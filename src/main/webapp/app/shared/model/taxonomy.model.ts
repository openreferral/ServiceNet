export interface ITaxonomy {
  id?: number;
  name?: string;
  vocabulary?: string;
  parentName?: string;
  parentId?: number;
  externalDbId?: string;
  providerName?: string;
  taxonomyId?: string;
  details?: string;
}

export const defaultValue: Readonly<ITaxonomy> = {};
