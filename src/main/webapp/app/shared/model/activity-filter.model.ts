import { Moment } from 'moment';

export const enum SearchOn {
  ORGANIZATION = 'ORGANIZATION',
  SERVICES = 'SERVICES',
  LOCATIONS = 'LOCATIONS'
}

export const enum DateFilter {
  LAST_7_DAYS = 'LAST_7_DAYS',
  LAST_30_DAYS = 'LAST_30_DAYS',
  DATE_RANGE = 'DATE_RANGE'
}

export interface IActivityFilter {
  id?: number;
  name?: string;
  citiesFilterList?: string[];
  regionFilterList?: string[];
  postalCodesFilterList?: string[];
  taxonomiesFilterList?: string[];
  searchOn?: SearchOn;
  searchFields?: string[];
  partnerFilterList?: string[];
  dateFilter?: DateFilter;
  fromDate?: Moment;
  toDate?: Moment;
  hiddenFilter?: boolean;
  showPartner?: boolean;
  userLogin?: string;
  userId?: number;
}

export const defaultValue: Readonly<IActivityFilter> = {
  hiddenFilter: false,
  showPartner: false
};
