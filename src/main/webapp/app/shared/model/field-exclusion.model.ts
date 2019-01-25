export interface IFieldExclusion {
  id?: number;
  fields?: string;
  entity?: string;
}

export const defaultValue: Readonly<IFieldExclusion> = {};
