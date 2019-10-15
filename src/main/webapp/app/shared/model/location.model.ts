import { IHolidaySchedule } from 'app/shared/model/holiday-schedule.model';
import { ILanguage } from 'app/shared/model/language.model';
import { IAccessibilityForDisabilities } from 'app/shared/model/accessibility-for-disabilities.model';
import { IGeocodingResult } from 'app/shared/model/geocoding-result.model';
import { Moment } from 'moment';

export interface ILocation {
  id?: number;
  name?: string;
  alternateName?: string;
  description?: any;
  organizationName?: string;
  organizationId?: number;
  transportation?: string;
  latitude?: number;
  longitude?: number;
  externalDbId?: string;
  providerName?: string;
  physicalAddressId?: number;
  postalAddressId?: number;
  regularScheduleId?: number;
  holidaySchedules?: IHolidaySchedule[];
  langs?: ILanguage[];
  accessibilities?: IAccessibilityForDisabilities[];
  geocodingResults?: IGeocodingResult[];
  lastVerifiedOn?: Moment;
  updatedAt?: Moment;
}

export const defaultValue: Readonly<ILocation> = {};
