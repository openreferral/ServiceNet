import axios from 'axios';
import {
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction,
  parseHeaderForLinks,
  loadMoreDataWhenScrolled
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShelter, defaultValue } from 'app/shared/model/shelter.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_SHELTER_LIST: 'shelter/FETCH_SHELTER_LIST',
  FETCH_MY_SHELTER_LIST: 'shelter/FETCH_MY_SHELTER_LIST',
  SEARCH_SHELTERS: 'shelter/SEARCH_SHELTERS',
  FETCH_SHELTER: 'shelter/FETCH_SHELTER',
  CREATE_SHELTER: 'shelter/CREATE_SHELTER',
  UPDATE_SHELTER: 'shelter/UPDATE_SHELTER',
  DELETE_SHELTER: 'shelter/DELETE_SHELTER',
  RESET: 'shelter/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShelter>,
  myShelters: [] as ReadonlyArray<IShelter>,
  entity: defaultValue,
  updating: false,
  links: { next: 0 },
  totalItems: 0,
  updateSuccess: false
};

export type ShelterState = Readonly<typeof initialState>;

// Reducer

export default (state: ShelterState = initialState, action): ShelterState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHELTER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MY_SHELTER_LIST):
    case REQUEST(ACTION_TYPES.SEARCH_SHELTERS):
    case REQUEST(ACTION_TYPES.FETCH_SHELTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHELTER):
    case REQUEST(ACTION_TYPES.UPDATE_SHELTER):
    case REQUEST(ACTION_TYPES.DELETE_SHELTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SHELTER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MY_SHELTER_LIST):
    case FAILURE(ACTION_TYPES.SEARCH_SHELTERS):
    case FAILURE(ACTION_TYPES.FETCH_SHELTER):
    case FAILURE(ACTION_TYPES.CREATE_SHELTER):
    case FAILURE(ACTION_TYPES.UPDATE_SHELTER):
    case FAILURE(ACTION_TYPES.DELETE_SHELTER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHELTER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.data.length
      };
    case SUCCESS(ACTION_TYPES.FETCH_MY_SHELTER_LIST):
      return {
        ...state,
        loading: false,
        myShelters: action.payload.data,
        totalItems: action.payload.data.length
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SHELTERS):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHELTER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHELTER):
    case SUCCESS(ACTION_TYPES.UPDATE_SHELTER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHELTER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = SERVICENET_API_URL + '/shelters';

// Actions
export const searchEntities = (search, page, size, sort, filter) => {
  const requestUrl = `${apiUrl}/search${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;

  const filterDataToSend = {
    ...filter,
    searchQuery: search
  };

  return {
    type: filter.userId ? ACTION_TYPES.FETCH_MY_SHELTER_LIST : ACTION_TYPES.SEARCH_SHELTERS,
    payload: axios.post<IShelter>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`, filterDataToSend)
  };
};

export const getEntities: ICrudGetAllAction<IShelter> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SHELTER_LIST,
    payload: axios.get<IShelter>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IShelter> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHELTER,
    payload: axios.get<IShelter>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShelter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHELTER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShelter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHELTER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShelter> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHELTER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
