export interface ILanguage {
  id?: number;
  language?: string;
  srvcName?: string;
  srvcId?: number;
  locationName?: string;
  locationId?: number;
}

export const defaultValue: Readonly<ILanguage> = {};
