import { Moment } from 'moment';
import { ISystemAccount } from 'app/shared/model//system-account.model';

export const enum ConflictStateEnum {
  PENDING = 'PENDING',
  ACCEPTED = 'ACCEPTED',
  REJECTED = 'REJECTED'
}

export interface IConflict {
  id?: any;
  currentValue?: string;
  currentValueDate?: Moment;
  offeredValue?: string;
  offeredValueDate?: Moment;
  fieldName?: string;
  entityPath?: string;
  state?: ConflictStateEnum;
  stateDate?: Moment;
  createdDate?: Moment;
  resourceId?: any;
  ownerId?: any;
  acceptedThisChanges?: ISystemAccount[];
}

export const defaultValue: Readonly<IConflict> = {};
