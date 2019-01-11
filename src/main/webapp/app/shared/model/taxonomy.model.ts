export interface ITaxonomy {
  id?: number;
  name?: string;
  vocabulary?: string;
  parentName?: string;
  parentId?: number;
  externalDbId?: string;
  providerName?: string;
}

export const defaultValue: Readonly<ITaxonomy> = {};
