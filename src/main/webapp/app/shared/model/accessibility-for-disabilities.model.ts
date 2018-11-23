export interface IAccessibilityForDisabilities {
  id?: number;
  accessibility?: string;
  details?: string;
  locationName?: string;
  locationId?: number;
}

export const defaultValue: Readonly<IAccessibilityForDisabilities> = {};
