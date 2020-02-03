export const enum ServiceTaxonomiesDetailsFields {
  TAXONOMY_NAME = 'TAXONOMY_NAME',
  TAXONOMY_DETAILS = 'TAXONOMY_DETAILS'
}

export interface IServiceTaxonomiesDetailsFieldsValue {
  id?: number;
  serviceTaxonomiesDetailsField?: ServiceTaxonomiesDetailsFields;
}

export const defaultValue: Readonly<IServiceTaxonomiesDetailsFieldsValue> = {};
