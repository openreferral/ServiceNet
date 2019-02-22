export interface IGeocodingResult {
  id?: number;
  address?: string;
  latitude?: number;
  longitude?: number;
}

export const defaultValue: Readonly<IGeocodingResult> = {};
