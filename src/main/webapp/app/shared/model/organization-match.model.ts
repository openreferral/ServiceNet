import { Moment } from 'moment';

export interface IOrganizationMatch {
  id?: number;
  fieldName?: string;
  timestamp?: Moment;
  deleted?: boolean;
  fieldPath?: string;
  matchedValue?: any;
  organizationRecordName?: string;
  organizationRecordId?: number;
  partnerVersionName?: string;
  partnerVersionId?: number;
}

export const defaultValue: Readonly<IOrganizationMatch> = {
  deleted: false
};
