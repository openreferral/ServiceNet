export const enum PhysicalAddressFields {
  ATTENTION = 'ATTENTION',
  ADDRESS_1 = 'ADDRESS_1',
  CITY = 'CITY',
  REGION = 'REGION',
  STATE_PROVINCE = 'STATE_PROVINCE',
  POSTAL_CODE = 'POSTAL_CODE',
  COUNTRY = 'COUNTRY'
}

export interface IPhysicalAddressFieldsValue {
  id?: number;
  physicalAddressField?: PhysicalAddressFields;
}

export const defaultValue: Readonly<IPhysicalAddressFieldsValue> = {};
