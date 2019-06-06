import { Moment } from 'moment';
import { IShelter } from 'app/shared/model/shelter.model';

export interface IBeds {
  id?: number;
  availableBeds?: number;
  waitlist?: number;
  updatedAt?: Moment;
  shelter?: IShelter;
}

export const defaultValue: Readonly<IBeds> = {};
