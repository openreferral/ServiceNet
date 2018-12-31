import { Moment } from 'moment';

export interface IDataImportReport {
  id?: number;
  numberOfUpdatedServices?: number;
  numberOfCreatedServices?: number;
  numberOfUpdatedOrgs?: number;
  numberOfCreatedOrgs?: number;
  startDate?: Moment;
  endDate?: Moment;
  documentUploadId?: number;
}

export const defaultValue: Readonly<IDataImportReport> = {};
