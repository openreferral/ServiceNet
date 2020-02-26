import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IServiceArea, defaultValue } from 'app/shared/model/service-area.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_SERVICEAREA_LIST: 'serviceArea/FETCH_SERVICEAREA_LIST',
  FETCH_SERVICEAREA: 'serviceArea/FETCH_SERVICEAREA',
  CREATE_SERVICEAREA: 'serviceArea/CREATE_SERVICEAREA',
  UPDATE_SERVICEAREA: 'serviceArea/UPDATE_SERVICEAREA',
  DELETE_SERVICEAREA: 'serviceArea/DELETE_SERVICEAREA',
  SET_BLOB: 'serviceArea/SET_BLOB',
  RESET: 'serviceArea/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IServiceArea>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ServiceAreaState = Readonly<typeof initialState>;

// Reducer

export default (state: ServiceAreaState = initialState, action): ServiceAreaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SERVICEAREA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SERVICEAREA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SERVICEAREA):
    case REQUEST(ACTION_TYPES.UPDATE_SERVICEAREA):
    case REQUEST(ACTION_TYPES.DELETE_SERVICEAREA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SERVICEAREA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SERVICEAREA):
    case FAILURE(ACTION_TYPES.CREATE_SERVICEAREA):
    case FAILURE(ACTION_TYPES.UPDATE_SERVICEAREA):
    case FAILURE(ACTION_TYPES.DELETE_SERVICEAREA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVICEAREA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVICEAREA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SERVICEAREA):
    case SUCCESS(ACTION_TYPES.UPDATE_SERVICEAREA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SERVICEAREA):
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

const apiUrl = SERVICENET_API_URL + '/service-areas';

// Actions

export const getEntities: ICrudGetAllAction<IServiceArea> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SERVICEAREA_LIST,
  payload: axios.get<IServiceArea>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IServiceArea> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SERVICEAREA,
    payload: axios.get<IServiceArea>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IServiceArea> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SERVICEAREA,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IServiceArea> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SERVICEAREA,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IServiceArea> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SERVICEAREA,
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
