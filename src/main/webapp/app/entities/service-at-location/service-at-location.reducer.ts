import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IServiceAtLocation, defaultValue } from 'app/shared/model/service-at-location.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_SERVICEATLOCATION_LIST: 'serviceAtLocation/FETCH_SERVICEATLOCATION_LIST',
  FETCH_SERVICEATLOCATION: 'serviceAtLocation/FETCH_SERVICEATLOCATION',
  CREATE_SERVICEATLOCATION: 'serviceAtLocation/CREATE_SERVICEATLOCATION',
  UPDATE_SERVICEATLOCATION: 'serviceAtLocation/UPDATE_SERVICEATLOCATION',
  DELETE_SERVICEATLOCATION: 'serviceAtLocation/DELETE_SERVICEATLOCATION',
  SET_BLOB: 'serviceAtLocation/SET_BLOB',
  RESET: 'serviceAtLocation/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IServiceAtLocation>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ServiceAtLocationState = Readonly<typeof initialState>;

// Reducer

export default (state: ServiceAtLocationState = initialState, action): ServiceAtLocationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SERVICEATLOCATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SERVICEATLOCATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SERVICEATLOCATION):
    case REQUEST(ACTION_TYPES.UPDATE_SERVICEATLOCATION):
    case REQUEST(ACTION_TYPES.DELETE_SERVICEATLOCATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SERVICEATLOCATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SERVICEATLOCATION):
    case FAILURE(ACTION_TYPES.CREATE_SERVICEATLOCATION):
    case FAILURE(ACTION_TYPES.UPDATE_SERVICEATLOCATION):
    case FAILURE(ACTION_TYPES.DELETE_SERVICEATLOCATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVICEATLOCATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVICEATLOCATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SERVICEATLOCATION):
    case SUCCESS(ACTION_TYPES.UPDATE_SERVICEATLOCATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SERVICEATLOCATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = SERVICENET_API_URL + '/service-at-locations';

// Actions

export const getEntities: ICrudGetAllAction<IServiceAtLocation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SERVICEATLOCATION_LIST,
    payload: axios.get<IServiceAtLocation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IServiceAtLocation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SERVICEATLOCATION,
    payload: axios.get<IServiceAtLocation>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IServiceAtLocation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SERVICEATLOCATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IServiceAtLocation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SERVICEATLOCATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IServiceAtLocation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SERVICEATLOCATION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
