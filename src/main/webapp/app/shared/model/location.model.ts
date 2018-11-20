import { ILanguage } from 'app/shared/model//language.model';
import { IAccessibilityForDisabilities } from 'app/shared/model//accessibility-for-disabilities.model';

export interface ILocation {
  id?: number;
  name?: string;
  alternameName?: string;
  description?: any;
  transportation?: string;
  latitude?: number;
  longitude?: number;
  physicalAddressId?: number;
  postalAddressId?: number;
  regularScheduleId?: number;
  holidayScheduleId?: number;
  langs?: ILanguage[];
  accessibilities?: IAccessibilityForDisabilities[];
}

export const defaultValue: Readonly<ILocation> = {};
