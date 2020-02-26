import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IServiceFieldsValue, defaultValue } from 'app/shared/model/service-fields-value.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_SERVICEFIELDSVALUE_LIST: 'serviceFieldsValue/FETCH_SERVICEFIELDSVALUE_LIST',
  FETCH_SERVICEFIELDSVALUE: 'serviceFieldsValue/FETCH_SERVICEFIELDSVALUE',
  CREATE_SERVICEFIELDSVALUE: 'serviceFieldsValue/CREATE_SERVICEFIELDSVALUE',
  UPDATE_SERVICEFIELDSVALUE: 'serviceFieldsValue/UPDATE_SERVICEFIELDSVALUE',
  DELETE_SERVICEFIELDSVALUE: 'serviceFieldsValue/DELETE_SERVICEFIELDSVALUE',
  RESET: 'serviceFieldsValue/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IServiceFieldsValue>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ServiceFieldsValueState = Readonly<typeof initialState>;

// Reducer

export default (state: ServiceFieldsValueState = initialState, action): ServiceFieldsValueState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SERVICEFIELDSVALUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SERVICEFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SERVICEFIELDSVALUE):
    case REQUEST(ACTION_TYPES.UPDATE_SERVICEFIELDSVALUE):
    case REQUEST(ACTION_TYPES.DELETE_SERVICEFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SERVICEFIELDSVALUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SERVICEFIELDSVALUE):
    case FAILURE(ACTION_TYPES.CREATE_SERVICEFIELDSVALUE):
    case FAILURE(ACTION_TYPES.UPDATE_SERVICEFIELDSVALUE):
    case FAILURE(ACTION_TYPES.DELETE_SERVICEFIELDSVALUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVICEFIELDSVALUE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVICEFIELDSVALUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SERVICEFIELDSVALUE):
    case SUCCESS(ACTION_TYPES.UPDATE_SERVICEFIELDSVALUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SERVICEFIELDSVALUE):
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

const apiUrl = SERVICENET_API_URL + '/service-fields-values';

// Actions

export const getEntities: ICrudGetAllAction<IServiceFieldsValue> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SERVICEFIELDSVALUE_LIST,
  payload: axios.get<IServiceFieldsValue>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IServiceFieldsValue> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SERVICEFIELDSVALUE,
    payload: axios.get<IServiceFieldsValue>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IServiceFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SERVICEFIELDSVALUE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IServiceFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SERVICEFIELDSVALUE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IServiceFieldsValue> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SERVICEFIELDSVALUE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
