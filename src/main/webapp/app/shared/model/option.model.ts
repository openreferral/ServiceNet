export const enum OptionType {
  LANGUAGE = 'LANGUAGE',
  DEFINED_COVERAGE_AREA = 'DEFINED_COVERAGE_AREA',
  TAG = 'TAG'
}

export interface IOption {
  id?: number;
  type?: OptionType;
  value?: number;
}

export const defaultValue: Readonly<IOption> = {};
