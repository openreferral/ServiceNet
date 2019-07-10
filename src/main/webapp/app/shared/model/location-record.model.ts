import { ILanguage } from 'app/shared/model//language.model';
import { IAccessibilityForDisabilities } from 'app/shared/model//accessibility-for-disabilities.model';
import { ILocation } from './location.model';
import { IPhysicalAddress } from './physical-address.model';
import { IPostalAddress } from './postal-address.model';
import { IOpeningHours } from './opening-hours.model';
import { IHolidaySchedule } from './holiday-schedule.model';

export interface ILocationRecord {
  location?: ILocation;
  physicalAddress?: IPhysicalAddress;
  postalAddress?: IPostalAddress;
  regularScheduleOpeningHours?: IOpeningHours[];
  holidaySchedules?: IHolidaySchedule[];
  langs?: ILanguage[];
  accessibilities?: IAccessibilityForDisabilities[];
}

export const defaultValue: Readonly<ILocationRecord> = {};
