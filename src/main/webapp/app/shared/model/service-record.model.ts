import { IRequiredDocument } from 'app/shared/model//required-document.model';
import { IPaymentAccepted } from 'app/shared/model//payment-accepted.model';
import { ILanguage } from 'app/shared/model//language.model';
import { IServiceTaxonomy } from 'app/shared/model//service-taxonomy.model';
import { IService } from './service.model';
import { IOpeningHours } from './opening-hours.model';
import { IHolidaySchedule } from './holiday-schedule.model';
import { IFunding } from './funding.model';
import { IEligibility } from './eligibility.model';
import { IPhone } from './phone.model';
import { IContact } from './contact.model';

export interface IServiceRecord {
  service?: IService;
  regularScheduleOpeningHours?: IOpeningHours[];
  holidaySchedule?: IHolidaySchedule;
  funding?: IFunding;
  eligibility?: IEligibility;
  docs?: IRequiredDocument[];
  paymentsAccepteds?: IPaymentAccepted[];
  langs?: ILanguage[];
  taxonomies?: IServiceTaxonomy[];
  phones?: IPhone[];
  contacts?: IContact[];
}

export const defaultValue: Readonly<IService> = {};
