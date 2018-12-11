export interface IOpeningHours {
  id?: number;
  weekday?: number;
  opensAt?: string;
  closesAt?: string;
  regularScheduleId?: number;
}

export const defaultValue: Readonly<IOpeningHours> = {};
