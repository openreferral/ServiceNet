import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { ORGANIZATION, getDefaultSearchFieldOptions } from 'app/modules/home/filter.constants';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_POSTAL_CODE_LIST: 'filterActivity/FETCH_POSTAL_CODE_LIST',
  FETCH_REGION_LIST: 'filterActivity/FETCH_REGION_LIST',
  FETCH_CITY_LIST: 'filterActivity/FETCH_CITY_LIST',
  FETCH_PARTNER_LIST: 'filterActivity/FETCH_PARTNER_LIST',
  FETCH_TAXONOMY_LIST: 'filterActivity/FETCH_TAXONOMY_LIST',
  UPDATE_ACTIVITY_FILTER: 'filterActivity/UPDATE_ACTIVITY_FILTER',
  DELETE_ACTIVITY_FILTER: 'filterActivity/DELETE_ACTIVITY_FILTER',
  FETCH_SAVED_FILTERS: 'filterActivity/FETCH_SAVED_FILTERS',
  RESET_ACTIVITY_FILTER: 'filterActivity/RESET_ACTIVITY_FILTER'
};

const initialState = {
  loading: false,
  errorMessage: null,
  currentProvider: null,
  postalCodeList: [],
  regionList: [],
  cityList: [],
  partnerList: [],
  taxonomyMap: [],
  savedFilters: [],
  activityFilter: {
    citiesFilterList: [],
    regionFilterList: [],
    postalCodesFilterList: [],
    partnerFilterList: [],
    taxonomiesFilterList: [],
    searchFields: getDefaultSearchFieldOptions().map(o => o.value),
    searchOn: ORGANIZATION,
    dateFilter: null,
    fromDate: '',
    toDate: '',
    hiddenFilter: false,
    showPartner: false,
    showOnlyHighlyMatched: false,
    applyLocationSearch: false,
    latitude: null,
    longitude: null,
    radius: 1
  }
};

export type FilterActivityState = Readonly<typeof initialState>;

export default (state: FilterActivityState = initialState, action): FilterActivityState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.DELETE_ACTIVITY_FILTER):
    case REQUEST(ACTION_TYPES.FETCH_POSTAL_CODE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_REGION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CITY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PARTNER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TAXONOMY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SAVED_FILTERS):
      return {
        ...state,
        errorMessage: null,
        loading: true
      };
    case FAILURE(ACTION_TYPES.FETCH_POSTAL_CODE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_REGION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CITY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PARTNER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TAXONOMY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SAVED_FILTERS):
      return {
        ...state,
        loading: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_POSTAL_CODE_LIST):
      return {
        ...state,
        postalCodeList: action.payload.data,
        loading: false
      };
    case SUCCESS(ACTION_TYPES.FETCH_REGION_LIST):
      return {
        ...state,
        regionList: action.payload.data,
        loading: false
      };
    case SUCCESS(ACTION_TYPES.FETCH_CITY_LIST):
      return {
        ...state,
        cityList: action.payload.data,
        loading: false
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARTNER_LIST):
      return {
        ...state,
        partnerList: action.payload.data,
        loading: false
      };
    case SUCCESS(ACTION_TYPES.FETCH_TAXONOMY_LIST):
      return {
        ...state,
        taxonomyMap: action.payload.data.taxonomiesByProvider,
        currentProvider: action.payload.data.currentProvider,
        loading: false
      };
    case SUCCESS(ACTION_TYPES.FETCH_SAVED_FILTERS):
      return {
        ...state,
        savedFilters: action.payload.data,
        loading: false
      };
    case ACTION_TYPES.UPDATE_ACTIVITY_FILTER:
      return {
        ...state,
        activityFilter: action.payload,
        loading: false
      };
    case ACTION_TYPES.RESET_ACTIVITY_FILTER:
      return {
        ...state,
        activityFilter: initialState.activityFilter
      };
    default:
      return state;
  }
};

// Actions

export const getPostalCodeList = () => {
  const requestUrl = `${SERVICENET_API_URL}/activity-filter/get-postal-codes`;
  return {
    type: ACTION_TYPES.FETCH_POSTAL_CODE_LIST,
    payload: axios.get<any>(requestUrl)
  };
};

export const getRegionList = () => {
  const requestUrl = `${SERVICENET_API_URL}/activity-filter/get-regions`;
  return {
    type: ACTION_TYPES.FETCH_REGION_LIST,
    payload: axios.get<any>(requestUrl)
  };
};

export const getCityList = () => {
  const requestUrl = `${SERVICENET_API_URL}/activity-filter/get-cities`;
  return {
    type: ACTION_TYPES.FETCH_CITY_LIST,
    payload: axios.get<any>(requestUrl)
  };
};

export const getPartnerList = () => {
  const requestUrl = `${SERVICENET_API_URL}/system-accounts`;
  return {
    type: ACTION_TYPES.FETCH_PARTNER_LIST,
    payload: axios.get<any>(requestUrl)
  };
};

export const getTaxonomyMap = () => {
  const requestUrl = `${SERVICENET_API_URL}/activity-filter/get-taxonomies`;
  return {
    type: ACTION_TYPES.FETCH_TAXONOMY_LIST,
    payload: axios.get<any>(requestUrl)
  };
};

export const updateActivityFilter = activityFilter => ({
  type: ACTION_TYPES.UPDATE_ACTIVITY_FILTER,
  payload: activityFilter
});

export const getSavedFilters = () => {
  const requestUrl = `${SERVICENET_API_URL}/activity-filter/get-user-filters`;
  return {
    type: ACTION_TYPES.FETCH_SAVED_FILTERS,
    payload: axios.get<any>(requestUrl)
  };
};

export const resetActivityFilter = () => ({
  type: ACTION_TYPES.RESET_ACTIVITY_FILTER
});
