export interface IRequiredDocument {
  id?: number;
  document?: string;
  srvcName?: string;
  srvcId?: number;
  externalDbId?: string;
  providerName?: string;
}

export const defaultValue: Readonly<IRequiredDocument> = {};
