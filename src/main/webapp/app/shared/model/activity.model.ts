import { IConflict } from 'app/shared/model//conflict.model';
import { IOrganization } from 'app/shared/model//organization.model';
import { IOrganizationMatch } from 'app/shared/model//organization-match.model';
import { Moment } from 'moment';

export interface IActivity {
  lastUpdated?: Moment;
  organization?: IOrganization;
  organizationMatches?: IOrganizationMatch[];
  conflicts?: IConflict[];
}

export const defaultValue: Readonly<IActivity> = {};
