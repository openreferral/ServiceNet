export interface IRequiredDocument {
  id?: number;
  document?: string;
  srvcName?: string;
  srvcId?: number;
}

export const defaultValue: Readonly<IRequiredDocument> = {};
