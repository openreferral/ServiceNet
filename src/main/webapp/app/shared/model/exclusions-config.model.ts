import { IFieldExclusion } from 'app/shared/model//field-exclusion.model';

export interface IExclusionsConfig {
  id?: number;
  accountId?: number;
  accountName?: string;
  exclusions?: IFieldExclusion[];
}

export const defaultValue: Readonly<IExclusionsConfig> = {};
