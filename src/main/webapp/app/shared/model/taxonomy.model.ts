export interface ITaxonomy {
  id?: number;
  name?: string;
  vocabulary?: string;
  parentName?: string;
  parentId?: number;
}

export const defaultValue: Readonly<ITaxonomy> = {};
