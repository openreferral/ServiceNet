export interface IConfidentialRecord {
  id?: number;
  resourceId?: string;
  fields?: string;
}

export const defaultValue: Readonly<IConfidentialRecord> = {};
