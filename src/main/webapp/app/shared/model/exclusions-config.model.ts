import { IFieldExclusion } from 'app/shared/model/field-exclusion.model';
import { ILocationExclusion } from 'app/shared/model/location-exclusion.model';

export interface IExclusionsConfig {
  id?: number;
  accountName?: string;
  accountId?: number;
  exclusions?: IFieldExclusion[];
  locationExclusions?: ILocationExclusion[];
}

export const defaultValue: Readonly<IExclusionsConfig> = {};
