import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILocationFieldsValue, defaultValue } from 'app/shared/model/location-fields-value.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_LOCATIONFIELDSVALUE_LIST: 'locationFieldsValue/FETCH_LOCATIONFIELDSVALUE_LIST',
  FETCH_LOCATIONFIELDSVALUE: 'locationFieldsValue/FETCH_LOCATIONFIELDSVALUE',
  CREATE_LOCATIONFIELDSVALUE: 'locationFieldsValue/CREATE_LOCATIONFIELDSVALUE',
  UPDATE_LOCATIONFIELDSVALUE: 'locationFieldsValue/UPDATE_LOCATIONFIELDSVALUE',
  DELETE_LOCATIONFIELDSVALUE: 'locationFieldsValue/DELETE_LOCATIONFIELDSVALUE',
  RESET: 'locationFieldsValue/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILocationFieldsValue>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type LocationFieldsValueState = Readonly<typeof initialState>;

// Reducer

export default (state: LocationFieldsValueState = initialState, action): LocationFieldsValueState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_LOCATIONFIELDSVALUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LOCATIONFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LOCATIONFIELDSVALUE):
    case REQUEST(ACTION_TYPES.UPDATE_LOCATIONFIELDSVALUE):
    case REQUEST(ACTION_TYPES.DELETE_LOCATIONFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_LOCATIONFIELDSVALUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LOCATIONFIELDSVALUE):
    case FAILURE(ACTION_TYPES.CREATE_LOCATIONFIELDSVALUE):
    case FAILURE(ACTION_TYPES.UPDATE_LOCATIONFIELDSVALUE):
    case FAILURE(ACTION_TYPES.DELETE_LOCATIONFIELDSVALUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOCATIONFIELDSVALUE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOCATIONFIELDSVALUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LOCATIONFIELDSVALUE):
    case SUCCESS(ACTION_TYPES.UPDATE_LOCATIONFIELDSVALUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LOCATIONFIELDSVALUE):
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

const apiUrl = SERVICENET_API_URL + '/location-fields-values';

// Actions

export const getEntities: ICrudGetAllAction<ILocationFieldsValue> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_LOCATIONFIELDSVALUE_LIST,
  payload: axios.get<ILocationFieldsValue>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ILocationFieldsValue> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LOCATIONFIELDSVALUE,
    payload: axios.get<ILocationFieldsValue>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILocationFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LOCATIONFIELDSVALUE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILocationFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LOCATIONFIELDSVALUE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILocationFieldsValue> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LOCATIONFIELDSVALUE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
