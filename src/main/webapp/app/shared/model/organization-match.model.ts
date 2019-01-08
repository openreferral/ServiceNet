import { Moment } from 'moment';

export interface IOrganizationMatch {
  id?: number;
  timestamp?: Moment;
  deleted?: boolean;
  organizationRecordName?: string;
  organizationRecordId?: number;
  partnerVersionName?: string;
  partnerVersionId?: number;
}

export const defaultValue: Readonly<IOrganizationMatch> = {
  deleted: false
};
