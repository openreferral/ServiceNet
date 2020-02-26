import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISystemAccount, defaultValue } from 'app/shared/model/system-account.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_SYSTEMACCOUNT_LIST: 'systemAccount/FETCH_SYSTEMACCOUNT_LIST',
  FETCH_SYSTEMACCOUNT: 'systemAccount/FETCH_SYSTEMACCOUNT',
  CREATE_SYSTEMACCOUNT: 'systemAccount/CREATE_SYSTEMACCOUNT',
  UPDATE_SYSTEMACCOUNT: 'systemAccount/UPDATE_SYSTEMACCOUNT',
  DELETE_SYSTEMACCOUNT: 'systemAccount/DELETE_SYSTEMACCOUNT',
  RESET: 'systemAccount/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISystemAccount>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type SystemAccountState = Readonly<typeof initialState>;

// Reducer

export default (state: SystemAccountState = initialState, action): SystemAccountState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SYSTEMACCOUNT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SYSTEMACCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SYSTEMACCOUNT):
    case REQUEST(ACTION_TYPES.UPDATE_SYSTEMACCOUNT):
    case REQUEST(ACTION_TYPES.DELETE_SYSTEMACCOUNT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SYSTEMACCOUNT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SYSTEMACCOUNT):
    case FAILURE(ACTION_TYPES.CREATE_SYSTEMACCOUNT):
    case FAILURE(ACTION_TYPES.UPDATE_SYSTEMACCOUNT):
    case FAILURE(ACTION_TYPES.DELETE_SYSTEMACCOUNT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SYSTEMACCOUNT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SYSTEMACCOUNT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SYSTEMACCOUNT):
    case SUCCESS(ACTION_TYPES.UPDATE_SYSTEMACCOUNT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SYSTEMACCOUNT):
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

const apiUrl = SERVICENET_API_URL + '/system-accounts';

// Actions

export const getEntities: ICrudGetAllAction<ISystemAccount> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SYSTEMACCOUNT_LIST,
  payload: axios.get<ISystemAccount>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ISystemAccount> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SYSTEMACCOUNT,
    payload: axios.get<ISystemAccount>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ISystemAccount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SYSTEMACCOUNT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISystemAccount> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SYSTEMACCOUNT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISystemAccount> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SYSTEMACCOUNT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
