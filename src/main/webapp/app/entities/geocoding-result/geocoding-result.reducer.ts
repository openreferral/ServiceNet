import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGeocodingResult, defaultValue } from 'app/shared/model/geocoding-result.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_GEOCODINGRESULT_LIST: 'geocodingResult/FETCH_GEOCODINGRESULT_LIST',
  FETCH_GEOCODINGRESULT: 'geocodingResult/FETCH_GEOCODINGRESULT',
  CREATE_GEOCODINGRESULT: 'geocodingResult/CREATE_GEOCODINGRESULT',
  UPDATE_GEOCODINGRESULT: 'geocodingResult/UPDATE_GEOCODINGRESULT',
  DELETE_GEOCODINGRESULT: 'geocodingResult/DELETE_GEOCODINGRESULT',
  RESET: 'geocodingResult/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGeocodingResult>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type GeocodingResultState = Readonly<typeof initialState>;

// Reducer

export default (state: GeocodingResultState = initialState, action): GeocodingResultState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GEOCODINGRESULT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GEOCODINGRESULT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_GEOCODINGRESULT):
    case REQUEST(ACTION_TYPES.UPDATE_GEOCODINGRESULT):
    case REQUEST(ACTION_TYPES.DELETE_GEOCODINGRESULT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_GEOCODINGRESULT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GEOCODINGRESULT):
    case FAILURE(ACTION_TYPES.CREATE_GEOCODINGRESULT):
    case FAILURE(ACTION_TYPES.UPDATE_GEOCODINGRESULT):
    case FAILURE(ACTION_TYPES.DELETE_GEOCODINGRESULT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_GEOCODINGRESULT_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_GEOCODINGRESULT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_GEOCODINGRESULT):
    case SUCCESS(ACTION_TYPES.UPDATE_GEOCODINGRESULT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_GEOCODINGRESULT):
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

const apiUrl = SERVICENET_API_URL + '/geocoding-results';

// Actions

export const getEntities: ICrudGetAllAction<IGeocodingResult> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_GEOCODINGRESULT_LIST,
    payload: axios.get<IGeocodingResult>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IGeocodingResult> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GEOCODINGRESULT,
    payload: axios.get<IGeocodingResult>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IGeocodingResult> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GEOCODINGRESULT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGeocodingResult> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GEOCODINGRESULT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGeocodingResult> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GEOCODINGRESULT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
