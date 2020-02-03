export const enum PostalAddressFields {
  ATTENTION = 'ATTENTION',
  ADDRESS1 = 'ADDRESS1',
  CITY = 'CITY',
  REGION = 'REGION',
  STATE_PROVINCE = 'STATE_PROVINCE',
  POSTAL_CODE = 'POSTAL_CODE',
  COUNTRY = 'COUNTRY'
}

export interface IPostalAddressFieldsValue {
  id?: number;
  postalAddressField?: PostalAddressFields;
}

export const defaultValue: Readonly<IPostalAddressFieldsValue> = {};
