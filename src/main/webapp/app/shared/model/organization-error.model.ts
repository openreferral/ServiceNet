import { IOrganization } from 'app/shared/model/organization.model';
import { IDataImportReport } from 'app/shared/model/data-import-report.model';

export interface IOrganizationError {
  id?: number;
  entityName?: string;
  fieldName?: string;
  externalDbId?: string;
  invalidValue?: string;
  cause?: string;
  organization?: IOrganization;
  dataImportReport?: IDataImportReport;
}

export const defaultValue: Readonly<IOrganizationError> = {};
