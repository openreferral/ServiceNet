import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IContactDetailsFieldsValue, defaultValue } from 'app/shared/model/contact-details-fields-value.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_CONTACTDETAILSFIELDSVALUE_LIST: 'contactDetailsFieldsValue/FETCH_CONTACTDETAILSFIELDSVALUE_LIST',
  FETCH_CONTACTDETAILSFIELDSVALUE: 'contactDetailsFieldsValue/FETCH_CONTACTDETAILSFIELDSVALUE',
  CREATE_CONTACTDETAILSFIELDSVALUE: 'contactDetailsFieldsValue/CREATE_CONTACTDETAILSFIELDSVALUE',
  UPDATE_CONTACTDETAILSFIELDSVALUE: 'contactDetailsFieldsValue/UPDATE_CONTACTDETAILSFIELDSVALUE',
  DELETE_CONTACTDETAILSFIELDSVALUE: 'contactDetailsFieldsValue/DELETE_CONTACTDETAILSFIELDSVALUE',
  RESET: 'contactDetailsFieldsValue/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IContactDetailsFieldsValue>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ContactDetailsFieldsValueState = Readonly<typeof initialState>;

// Reducer

export default (state: ContactDetailsFieldsValueState = initialState, action): ContactDetailsFieldsValueState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CONTACTDETAILSFIELDSVALUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CONTACTDETAILSFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CONTACTDETAILSFIELDSVALUE):
    case REQUEST(ACTION_TYPES.UPDATE_CONTACTDETAILSFIELDSVALUE):
    case REQUEST(ACTION_TYPES.DELETE_CONTACTDETAILSFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CONTACTDETAILSFIELDSVALUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CONTACTDETAILSFIELDSVALUE):
    case FAILURE(ACTION_TYPES.CREATE_CONTACTDETAILSFIELDSVALUE):
    case FAILURE(ACTION_TYPES.UPDATE_CONTACTDETAILSFIELDSVALUE):
    case FAILURE(ACTION_TYPES.DELETE_CONTACTDETAILSFIELDSVALUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTACTDETAILSFIELDSVALUE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONTACTDETAILSFIELDSVALUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CONTACTDETAILSFIELDSVALUE):
    case SUCCESS(ACTION_TYPES.UPDATE_CONTACTDETAILSFIELDSVALUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CONTACTDETAILSFIELDSVALUE):
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

const apiUrl = SERVICENET_API_URL + '/contact-details-fields-values';

// Actions

export const getEntities: ICrudGetAllAction<IContactDetailsFieldsValue> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CONTACTDETAILSFIELDSVALUE_LIST,
  payload: axios.get<IContactDetailsFieldsValue>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IContactDetailsFieldsValue> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CONTACTDETAILSFIELDSVALUE,
    payload: axios.get<IContactDetailsFieldsValue>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IContactDetailsFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CONTACTDETAILSFIELDSVALUE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IContactDetailsFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CONTACTDETAILSFIELDSVALUE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IContactDetailsFieldsValue> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CONTACTDETAILSFIELDSVALUE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
