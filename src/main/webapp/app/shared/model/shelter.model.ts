import { IPhone } from 'app/shared/model/phone.model';
import { IOption } from 'app/shared/model/option.model';
import { IBeds } from 'app/shared/model/beds.model';

export interface IShelter {
  id?: number;
  agencyName?: string;
  programName?: string;
  alternateName?: string;
  website?: string;
  eligibilityDetails?: string;
  documentsRequired?: string;
  applicationProcess?: string;
  fees?: string;
  programHours?: string;
  holidaySchedule?: string;
  emails?: string[];
  address1?: string;
  address2?: string;
  city?: string;
  zipcode?: string;
  locationDescription?: string;
  busService?: string;
  transportation?: string;
  disabilityAccess?: string;
  phones?: IPhone[];
  beds?: IBeds;
  tags?: IOption[];
  languages?: IOption[];
  definedCoverageAreas?: IOption[];
}

export const defaultValue: Readonly<IShelter> = {};
