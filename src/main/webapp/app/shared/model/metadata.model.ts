import { Moment } from 'moment';

export const enum ActionType {
  CREATE = 'CREATE',
  UPDATE = 'UPDATE',
  DELETE = 'DELETE'
}

export interface IMetadata {
  id?: number;
  resourceId?: string;
  lastActionDate?: Moment;
  lastActionType?: ActionType;
  fieldName?: string;
  previousValue?: any;
  replacementValue?: any;
  userLogin?: string;
  userId?: number;
}

export const defaultValue: Readonly<IMetadata> = {};
