import { translate } from 'react-jhipster';

export const ORGANIZATION = 'ORGANIZATION';
export const SERVICES = 'SERVICES';
export const LOCATIONS = 'LOCATIONS';

const NAME = 'name';
const ALTERNATE_NAME = 'alternateName';
const DESCRIPTION = 'description';
const EMAIL = 'email';
const URL = 'url';
const TAX_STATUS = 'taxStatus';
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

const OPTION_MAP = {
  [ORGANIZATION]: [NAME, ALTERNATE_NAME, DESCRIPTION, EMAIL, URL, TAX_STATUS, TAX_ID, LEGAL_STATUS, EXTERNAL_DB_ID],
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
    PROVIDER_NAME
  ],
  [LOCATIONS]: [NAME, ALTERNATE_NAME, DESCRIPTION, TRANSPORTATION, EXTERNAL_DB_ID, PROVIDER_NAME]
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
