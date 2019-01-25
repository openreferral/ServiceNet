import { ILanguage } from 'app/shared/model//language.model';
import { IAccessibilityForDisabilities } from 'app/shared/model//accessibility-for-disabilities.model';

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
  holidayScheduleId?: number;
  langs?: ILanguage[];
  accessibilities?: IAccessibilityForDisabilities[];
}

export const defaultValue: Readonly<ILocation> = {};
