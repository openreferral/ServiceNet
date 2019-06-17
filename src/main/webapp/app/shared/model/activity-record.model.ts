import { IConflict } from 'app/shared/model//conflict.model';
import { IOrganization } from './organization.model';
import { Moment } from 'moment';
import { IContact } from './contact.model';
import { IFieldExclusion } from './field-exclusion.model';

export interface IActivityRecord {
  organization?: IOrganization;
  lastUpdated?: Moment;
  locations?: any;
  services?: any;
  contacts?: IContact[];
  exclusions?: IFieldExclusion[];
  conflicts?: IConflict[];
}

export const defaultValue: Readonly<IActivityRecord> = {};
