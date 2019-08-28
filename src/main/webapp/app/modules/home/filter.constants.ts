import { translate } from 'react-jhipster';

export const ORGANIZATION = 'ORGANIZATION';
export const SERVICES = 'SERVICES';
export const LOCATIONS = 'LOCATIONS';

const NAME = 'name';
const ALTERNATE_NAME = 'alternateName';
const DESCRIPTION = 'description';
const EMAIL = 'email';
const URL = 'url';
const TAX_ID = 'taxId';
const LEGAL_STATUS = 'legalStatus';
const EXTERNAL_DB_ID = 'externalDbId';
const STATUS = 'status';
const INTERPRETATION_SERVICES = 'interpretationServices';
const APPLICATION_PROCESS = 'applicationProcess';
const WAIT_TIME = 'waitTime';
const FEES = 'fees';
const ACCREDITATIONS = 'accreditations';
const LICENSES = 'licenses';
const TYPE = 'type';
const PROVIDER_NAME = 'providerName';
const TRANSPORTATION = 'transportation';
const PHONE = 'phone';
const CONTACT_NAME = 'contactName';
const CONTACT_PHONE = 'contactPhone';
const ELIGIBILITY = 'eligibility';
const REQUIRED_DOCUMENT = 'requiredDocument';
const LANGUAGE = 'language';
const PHYSICAL_ADDRESS = 'physicalAddress';
const POSTAL_ADDRESS = 'postalAddress';
const ACCESSIBILITY = 'accessibility';

const OPTION_MAP = {
  [ORGANIZATION]: [NAME, ALTERNATE_NAME, DESCRIPTION, EMAIL, URL, TAX_ID, LEGAL_STATUS, EXTERNAL_DB_ID, PHONE, CONTACT_NAME, CONTACT_PHONE],
  [SERVICES]: [
    NAME,
    ALTERNATE_NAME,
    DESCRIPTION,
    EMAIL,
    URL,
    STATUS,
    INTERPRETATION_SERVICES,
    APPLICATION_PROCESS,
    WAIT_TIME,
    FEES,
    ACCREDITATIONS,
    LICENSES,
    TYPE,
    EXTERNAL_DB_ID,
    PROVIDER_NAME,
    ELIGIBILITY,
    REQUIRED_DOCUMENT,
    LANGUAGE,
    PHONE,
    CONTACT_NAME,
    CONTACT_PHONE
  ],
  [LOCATIONS]: [
    NAME,
    ALTERNATE_NAME,
    DESCRIPTION,
    TRANSPORTATION,
    EXTERNAL_DB_ID,
    PROVIDER_NAME,
    PHYSICAL_ADDRESS,
    POSTAL_ADDRESS,
    ACCESSIBILITY,
    PHONE
  ]
};

function getOption(field) {
  return {
    value: field,
    label: translate('serviceNetApp.activity.home.filter.fields.' + field)
  };
}

export function getSearchFieldOptions(searchOn) {
  if (!searchOn) {
    return [];
  }
  return OPTION_MAP[searchOn].map(getOption);
}

export function getDefaultSearchFieldOptions() {
  return [NAME, ALTERNATE_NAME].map(getOption);
}
