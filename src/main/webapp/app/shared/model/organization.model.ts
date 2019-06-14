import { Moment } from 'moment';
import { IProgram } from 'app/shared/model//program.model';
import { IService } from 'app/shared/model//service.model';
import { ILocation } from 'app/shared/model//location.model';

export interface IOrganization {
  id?: number;
  name?: string;
  alternateName?: string;
  description?: any;
  email?: string;
  url?: string;
  taxStatus?: string;
  taxId?: string;
  yearIncorporated?: Moment;
  legalStatus?: string;
  active?: boolean;
  updatedAt?: Moment;
  externalDbId?: string;
  replacedById?: number;
  sourceDocumentDateUploaded?: string;
  sourceDocumentId?: number;
  accountName?: string;
  accountId?: number;
}

export const defaultValue: Readonly<IOrganization> = {
  active: false
};
