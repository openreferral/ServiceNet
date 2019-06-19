import { FAILURE, REQUEST, SUCCESS } from 'app/shared/reducers/action-type.util';
import axios from 'axios';

export const ACTION_TYPES = {
  FETCH_REGION_LIST: 'filterShelter/FETCH_REGION_LIST',
  UPDATE_SHELTER_FILTER: 'filterShelter/UPDATE_SHELTER_FILTER'
};

export const initialState = {
  loading: false,
  errorMessage: null,
  regionList: [],
  shelterFilter: { regionFilterList: [] }
};

export type FilterShelterState = Readonly<typeof initialState>;

export default (state: FilterShelterState = initialState, action): FilterShelterState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_REGION_LIST):
      return {
        ...state,
        errorMessage: null,
        loading: true
      };
    case FAILURE(ACTION_TYPES.FETCH_REGION_LIST):
      return {
        ...state,
        loading: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_REGION_LIST):
      return {
        ...state,
        regionList: action.payload.data,
        loading: false
      };
    case ACTION_TYPES.UPDATE_SHELTER_FILTER:
      return {
        ...state,
        shelterFilter: action.payload,
        loading: false
      };
    default:
      return state;
  }
};

// Actions
export const getRegionList = () => {
  const requestUrl = `api/activity-filter/get-regions`;
  return {
    type: ACTION_TYPES.FETCH_REGION_LIST,
    payload: axios.get<any>(requestUrl)
  };
};

// tslint:disable-next-line:ter-arrow-body-style
export const updateShelterFilter = shelterFilter => {
  return {
    type: ACTION_TYPES.UPDATE_SHELTER_FILTER,
    payload: shelterFilter
  };
};
