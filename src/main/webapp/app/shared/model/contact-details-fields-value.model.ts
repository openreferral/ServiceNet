export const enum ContactDetailsFields {
  NAME = 'NAME',
  TITLE = 'TITLE',
  DEPARTMENT = 'DEPARTMENT',
  EMAIL = 'EMAIL'
}

export interface IContactDetailsFieldsValue {
  id?: number;
  contactDetailsField?: ContactDetailsFields;
}

export const defaultValue: Readonly<IContactDetailsFieldsValue> = {};
