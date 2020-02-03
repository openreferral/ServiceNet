export interface IFieldsDisplaySettings {
  id?: number;
  name?: string;
  locationFields?: string;
  organizationFields?: string;
  physicalAddressFields?: string;
  postalAddressFields?: string;
  serviceFields?: string;
  serviceTaxonomiesDetailsFields?: string;
  contactDetailsFields?: string;
  userLogin?: string;
  userId?: number;
}

export const defaultValue: Readonly<IFieldsDisplaySettings> = {};
