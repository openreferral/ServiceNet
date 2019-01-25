import { IFieldExclusion } from 'app/shared/model//field-exclusion.model';

export interface IExclusionsConfig {
  id?: number;
  accountId?: number;
  exclusions?: IFieldExclusion[];
}

export const defaultValue: Readonly<IExclusionsConfig> = {};
