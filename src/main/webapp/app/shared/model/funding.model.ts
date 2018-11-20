export interface IFunding {
  id?: number;
  source?: string;
  organizationName?: string;
  organizationId?: number;
  srvcName?: string;
  srvcId?: number;
}

export const defaultValue: Readonly<IFunding> = {};
