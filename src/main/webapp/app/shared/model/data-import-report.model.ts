import { Moment } from 'moment';

export interface IDataImportReport {
  id?: number;
  numberOfUpdatedServices?: number;
  numberOfCreatedServices?: number;
  numberOfUpdatedOrgs?: number;
  numberOfCreatedOrgs?: number;
  startDate?: Moment;
  endDate?: Moment;
  userLogin?: string;
  userId?: number;
}

export const defaultValue: Readonly<IDataImportReport> = {};
