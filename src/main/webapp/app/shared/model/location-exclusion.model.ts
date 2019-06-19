export interface ILocationExclusion {
  id?: number;
  region?: string;
  city?: string;
  configId?: number;
}

export const defaultValue: Readonly<ILocationExclusion> = {};
