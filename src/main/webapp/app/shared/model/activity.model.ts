import { IConflict } from 'app/shared/model//conflict.model';
import { IOrganization } from 'app/shared/model//organization.model';
import { Moment } from 'moment';

export interface IActivity {
  lastUpdated?: Moment;
  organization?: IOrganization;
  conflicts?: IConflict[];
}

export const defaultValue: Readonly<IActivity> = {};
