import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOrganizationFieldsValue, defaultValue } from 'app/shared/model/organization-fields-value.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_ORGANIZATIONFIELDSVALUE_LIST: 'organizationFieldsValue/FETCH_ORGANIZATIONFIELDSVALUE_LIST',
  FETCH_ORGANIZATIONFIELDSVALUE: 'organizationFieldsValue/FETCH_ORGANIZATIONFIELDSVALUE',
  CREATE_ORGANIZATIONFIELDSVALUE: 'organizationFieldsValue/CREATE_ORGANIZATIONFIELDSVALUE',
  UPDATE_ORGANIZATIONFIELDSVALUE: 'organizationFieldsValue/UPDATE_ORGANIZATIONFIELDSVALUE',
  DELETE_ORGANIZATIONFIELDSVALUE: 'organizationFieldsValue/DELETE_ORGANIZATIONFIELDSVALUE',
  RESET: 'organizationFieldsValue/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOrganizationFieldsValue>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type OrganizationFieldsValueState = Readonly<typeof initialState>;

// Reducer

export default (state: OrganizationFieldsValueState = initialState, action): OrganizationFieldsValueState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ORGANIZATIONFIELDSVALUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ORGANIZATIONFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ORGANIZATIONFIELDSVALUE):
    case REQUEST(ACTION_TYPES.UPDATE_ORGANIZATIONFIELDSVALUE):
    case REQUEST(ACTION_TYPES.DELETE_ORGANIZATIONFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ORGANIZATIONFIELDSVALUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ORGANIZATIONFIELDSVALUE):
    case FAILURE(ACTION_TYPES.CREATE_ORGANIZATIONFIELDSVALUE):
    case FAILURE(ACTION_TYPES.UPDATE_ORGANIZATIONFIELDSVALUE):
    case FAILURE(ACTION_TYPES.DELETE_ORGANIZATIONFIELDSVALUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORGANIZATIONFIELDSVALUE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORGANIZATIONFIELDSVALUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ORGANIZATIONFIELDSVALUE):
    case SUCCESS(ACTION_TYPES.UPDATE_ORGANIZATIONFIELDSVALUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ORGANIZATIONFIELDSVALUE):
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

const apiUrl = SERVICENET_API_URL + '/organization-fields-values';

// Actions

export const getEntities: ICrudGetAllAction<IOrganizationFieldsValue> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ORGANIZATIONFIELDSVALUE_LIST,
  payload: axios.get<IOrganizationFieldsValue>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IOrganizationFieldsValue> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ORGANIZATIONFIELDSVALUE,
    payload: axios.get<IOrganizationFieldsValue>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOrganizationFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ORGANIZATIONFIELDSVALUE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOrganizationFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ORGANIZATIONFIELDSVALUE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOrganizationFieldsValue> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ORGANIZATIONFIELDSVALUE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
