import { Moment } from 'moment';

export interface IOrganizationMatch {
  id?: number;
  timestamp?: Moment;
  dismissed?: boolean;
  dismissComment?: string;
  dismissedById?: number;
  dismissedByName?: string;
  dismissDate?: Moment;
  organizationRecordName?: string;
  organizationRecordId?: number;
  partnerVersionName?: string;
  partnerVersionId?: number;
}

export const defaultValue: Readonly<IOrganizationMatch> = {
  dismissed: false
};
