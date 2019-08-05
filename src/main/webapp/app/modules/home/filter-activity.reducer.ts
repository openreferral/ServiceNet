import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  FETCH_POSTAL_CODE_LIST: 'filterActivity/FETCH_POSTAL_CODE_LIST',
  FETCH_REGION_LIST: 'filterActivity/FETCH_REGION_LIST',
  FETCH_CITY_LIST: 'filterActivity/FETCH_CITY_LIST',
  FETCH_PARTNER_LIST: 'filterActivity/FETCH_PARTNER_LIST',
  FETCH_TAXONOMY_LIST: 'filterActivity/FETCH_TAXONOMY_LIST',
  UPDATE_ACTIVITY_FILTER: 'filterActivity/UPDATE_ACTIVITY_FILTER'
};

const initialState = {
  loading: false,
  errorMessage: null,
  postalCodeList: [],
  regionList: [],
  cityList: [],
  partnerList: [],
  taxonomyList: [],
  activityFilter: {
    citiesFilterList: [],
    regionFilterList: [],
    postalCodesFilterList: [],
    partnerFilterList: [],
    taxonomiesFilterList: [],
    searchOn: '',
    dateFilter: null,
    fromDate: '',
    toDate: '',
    hidden: false
  }
};

export type FilterActivityState = Readonly<typeof initialState>;

export default (state: FilterActivityState = initialState, action): FilterActivityState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_POSTAL_CODE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_REGION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CITY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PARTNER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TAXONOMY_LIST):
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
        taxonomyList: action.payload.data,
        loading: false
      };
    case ACTION_TYPES.UPDATE_ACTIVITY_FILTER:
      return {
        ...state,
        activityFilter: action.payload,
        loading: false
      };
    default:
      return state;
  }
};

// Actions

export const getPostalCodeList = () => {
  const requestUrl = `api/activity-filter/get-postal-codes`;
  return {
    type: ACTION_TYPES.FETCH_POSTAL_CODE_LIST,
    payload: axios.get<any>(requestUrl)
  };
};

export const getRegionList = () => {
  const requestUrl = `api/activity-filter/get-regions`;
  return {
    type: ACTION_TYPES.FETCH_REGION_LIST,
    payload: axios.get<any>(requestUrl)
  };
};

export const getCityList = () => {
  const requestUrl = `api/activity-filter/get-cities`;
  return {
    type: ACTION_TYPES.FETCH_CITY_LIST,
    payload: axios.get<any>(requestUrl)
  };
};

export const getPartnerList = () => {
  const requestUrl = `api/system-accounts`;
  return {
    type: ACTION_TYPES.FETCH_PARTNER_LIST,
    payload: axios.get<any>(requestUrl)
  };
};

export const getTaxonomyList = () => {
  const requestUrl = `api/activity-filter/get-taxonomies`;
  return {
    type: ACTION_TYPES.FETCH_TAXONOMY_LIST,
    payload: axios.get<any>(requestUrl)
  };
};

// tslint:disable-next-line:ter-arrow-body-style
export const updateActivityFilter = activityFilter => {
  return {
    type: ACTION_TYPES.UPDATE_ACTIVITY_FILTER,
    payload: activityFilter
  };
};
