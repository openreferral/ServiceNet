import { Moment } from 'moment';
import { IOrganizationError } from 'app/shared/model/organization-error.model';

export interface IDataImportReport {
  id?: number;
  numberOfUpdatedServices?: number;
  numberOfCreatedServices?: number;
  numberOfUpdatedOrgs?: number;
  numberOfCreatedOrgs?: number;
  startDate?: Moment;
  endDate?: Moment;
  jobName?: string;
  errorMessage?: any;
  documentUploadId?: number;
  organizationErrors?: IOrganizationError[];
}

export const defaultValue: Readonly<IDataImportReport> = {};
