import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPhysicalAddressFieldsValue, defaultValue } from 'app/shared/model/physical-address-fields-value.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_PHYSICALADDRESSFIELDSVALUE_LIST: 'physicalAddressFieldsValue/FETCH_PHYSICALADDRESSFIELDSVALUE_LIST',
  FETCH_PHYSICALADDRESSFIELDSVALUE: 'physicalAddressFieldsValue/FETCH_PHYSICALADDRESSFIELDSVALUE',
  CREATE_PHYSICALADDRESSFIELDSVALUE: 'physicalAddressFieldsValue/CREATE_PHYSICALADDRESSFIELDSVALUE',
  UPDATE_PHYSICALADDRESSFIELDSVALUE: 'physicalAddressFieldsValue/UPDATE_PHYSICALADDRESSFIELDSVALUE',
  DELETE_PHYSICALADDRESSFIELDSVALUE: 'physicalAddressFieldsValue/DELETE_PHYSICALADDRESSFIELDSVALUE',
  RESET: 'physicalAddressFieldsValue/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPhysicalAddressFieldsValue>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PhysicalAddressFieldsValueState = Readonly<typeof initialState>;

// Reducer

export default (state: PhysicalAddressFieldsValueState = initialState, action): PhysicalAddressFieldsValueState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PHYSICALADDRESSFIELDSVALUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PHYSICALADDRESSFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PHYSICALADDRESSFIELDSVALUE):
    case REQUEST(ACTION_TYPES.UPDATE_PHYSICALADDRESSFIELDSVALUE):
    case REQUEST(ACTION_TYPES.DELETE_PHYSICALADDRESSFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PHYSICALADDRESSFIELDSVALUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PHYSICALADDRESSFIELDSVALUE):
    case FAILURE(ACTION_TYPES.CREATE_PHYSICALADDRESSFIELDSVALUE):
    case FAILURE(ACTION_TYPES.UPDATE_PHYSICALADDRESSFIELDSVALUE):
    case FAILURE(ACTION_TYPES.DELETE_PHYSICALADDRESSFIELDSVALUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PHYSICALADDRESSFIELDSVALUE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PHYSICALADDRESSFIELDSVALUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PHYSICALADDRESSFIELDSVALUE):
    case SUCCESS(ACTION_TYPES.UPDATE_PHYSICALADDRESSFIELDSVALUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PHYSICALADDRESSFIELDSVALUE):
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

const apiUrl = SERVICENET_API_URL + '/physical-address-fields-values';

// Actions

export const getEntities: ICrudGetAllAction<IPhysicalAddressFieldsValue> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PHYSICALADDRESSFIELDSVALUE_LIST,
  payload: axios.get<IPhysicalAddressFieldsValue>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPhysicalAddressFieldsValue> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PHYSICALADDRESSFIELDSVALUE,
    payload: axios.get<IPhysicalAddressFieldsValue>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPhysicalAddressFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PHYSICALADDRESSFIELDSVALUE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPhysicalAddressFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PHYSICALADDRESSFIELDSVALUE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPhysicalAddressFieldsValue> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PHYSICALADDRESSFIELDSVALUE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
