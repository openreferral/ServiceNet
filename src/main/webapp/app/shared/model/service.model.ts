import { Moment } from 'moment';
import { IServiceArea } from 'app/shared/model//service-area.model';
import { IRequiredDocument } from 'app/shared/model//required-document.model';
import { IPaymentAccepted } from 'app/shared/model//payment-accepted.model';
import { ILanguage } from 'app/shared/model//language.model';
import { IServiceTaxonomy } from 'app/shared/model//service-taxonomy.model';

export interface IService {
  id?: number;
  name?: string;
  alternateName?: string;
  description?: any;
  url?: string;
  email?: string;
  status?: string;
  interpretationServices?: any;
  applicationProcess?: any;
  waitTime?: any;
  fees?: any;
  accreditations?: any;
  licenses?: any;
  type?: string;
  updatedAt?: Moment;
  externalDbId?: string;
  providerName?: string;
  organizationName?: string;
  organizationId?: number;
  programName?: string;
  programId?: number;
  locationId?: number;
  regularScheduleId?: number;
  holidayScheduleId?: number;
  fundingId?: number;
  eligibilityId?: number;
  areas?: IServiceArea[];
  docs?: IRequiredDocument[];
  paymentsAccepteds?: IPaymentAccepted[];
  langs?: ILanguage[];
  taxonomies?: IServiceTaxonomy[];
  lastVerifiedOn?: Moment;
}

export const defaultValue: Readonly<IService> = {};
