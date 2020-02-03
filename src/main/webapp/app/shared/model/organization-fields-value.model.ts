export const enum OrganizationFields {
  NAME = 'NAME',
  ALTERNATE_NAME = 'ALTERNATE_NAME',
  DESCRIPTION = 'DESCRIPTION',
  EMAIL = 'EMAIL',
  URL = 'URL',
  TAX_STATUS = 'TAX_STATUS',
  ACTIVE = 'ACTIVE'
}

export interface IOrganizationFieldsValue {
  id?: number;
  organizationField?: OrganizationFields;
}

export const defaultValue: Readonly<IOrganizationFieldsValue> = {};
