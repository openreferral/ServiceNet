import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPostalAddressFieldsValue, defaultValue } from 'app/shared/model/postal-address-fields-value.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_POSTALADDRESSFIELDSVALUE_LIST: 'postalAddressFieldsValue/FETCH_POSTALADDRESSFIELDSVALUE_LIST',
  FETCH_POSTALADDRESSFIELDSVALUE: 'postalAddressFieldsValue/FETCH_POSTALADDRESSFIELDSVALUE',
  CREATE_POSTALADDRESSFIELDSVALUE: 'postalAddressFieldsValue/CREATE_POSTALADDRESSFIELDSVALUE',
  UPDATE_POSTALADDRESSFIELDSVALUE: 'postalAddressFieldsValue/UPDATE_POSTALADDRESSFIELDSVALUE',
  DELETE_POSTALADDRESSFIELDSVALUE: 'postalAddressFieldsValue/DELETE_POSTALADDRESSFIELDSVALUE',
  RESET: 'postalAddressFieldsValue/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPostalAddressFieldsValue>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PostalAddressFieldsValueState = Readonly<typeof initialState>;

// Reducer

export default (state: PostalAddressFieldsValueState = initialState, action): PostalAddressFieldsValueState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_POSTALADDRESSFIELDSVALUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_POSTALADDRESSFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_POSTALADDRESSFIELDSVALUE):
    case REQUEST(ACTION_TYPES.UPDATE_POSTALADDRESSFIELDSVALUE):
    case REQUEST(ACTION_TYPES.DELETE_POSTALADDRESSFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_POSTALADDRESSFIELDSVALUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_POSTALADDRESSFIELDSVALUE):
    case FAILURE(ACTION_TYPES.CREATE_POSTALADDRESSFIELDSVALUE):
    case FAILURE(ACTION_TYPES.UPDATE_POSTALADDRESSFIELDSVALUE):
    case FAILURE(ACTION_TYPES.DELETE_POSTALADDRESSFIELDSVALUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_POSTALADDRESSFIELDSVALUE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_POSTALADDRESSFIELDSVALUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_POSTALADDRESSFIELDSVALUE):
    case SUCCESS(ACTION_TYPES.UPDATE_POSTALADDRESSFIELDSVALUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_POSTALADDRESSFIELDSVALUE):
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

const apiUrl = SERVICENET_API_URL + '/postal-address-fields-values';

// Actions

export const getEntities: ICrudGetAllAction<IPostalAddressFieldsValue> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_POSTALADDRESSFIELDSVALUE_LIST,
  payload: axios.get<IPostalAddressFieldsValue>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPostalAddressFieldsValue> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_POSTALADDRESSFIELDSVALUE,
    payload: axios.get<IPostalAddressFieldsValue>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPostalAddressFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_POSTALADDRESSFIELDSVALUE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPostalAddressFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_POSTALADDRESSFIELDSVALUE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPostalAddressFieldsValue> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_POSTALADDRESSFIELDSVALUE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
