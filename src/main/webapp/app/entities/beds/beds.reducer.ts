import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IBeds, defaultValue } from 'app/shared/model/beds.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_BEDS_LIST: 'beds/FETCH_BEDS_LIST',
  FETCH_BEDS: 'beds/FETCH_BEDS',
  CREATE_BEDS: 'beds/CREATE_BEDS',
  UPDATE_BEDS: 'beds/UPDATE_BEDS',
  DELETE_BEDS: 'beds/DELETE_BEDS',
  RESET: 'beds/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IBeds>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type BedsState = Readonly<typeof initialState>;

// Reducer

export default (state: BedsState = initialState, action): BedsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BEDS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_BEDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_BEDS):
    case REQUEST(ACTION_TYPES.UPDATE_BEDS):
    case REQUEST(ACTION_TYPES.DELETE_BEDS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_BEDS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_BEDS):
    case FAILURE(ACTION_TYPES.CREATE_BEDS):
    case FAILURE(ACTION_TYPES.UPDATE_BEDS):
    case FAILURE(ACTION_TYPES.DELETE_BEDS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BEDS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_BEDS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_BEDS):
    case SUCCESS(ACTION_TYPES.UPDATE_BEDS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_BEDS):
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

const apiUrl = SERVICENET_API_URL + '/beds';

// Actions

export const getEntities: ICrudGetAllAction<IBeds> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_BEDS_LIST,
    payload: axios.get<IBeds>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IBeds> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_BEDS,
    payload: axios.get<IBeds>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IBeds> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_BEDS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IBeds> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_BEDS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IBeds> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_BEDS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
