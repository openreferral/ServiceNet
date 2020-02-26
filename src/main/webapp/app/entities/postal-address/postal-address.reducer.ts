import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPostalAddress, defaultValue } from 'app/shared/model/postal-address.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_POSTALADDRESS_LIST: 'postalAddress/FETCH_POSTALADDRESS_LIST',
  FETCH_POSTALADDRESS: 'postalAddress/FETCH_POSTALADDRESS',
  CREATE_POSTALADDRESS: 'postalAddress/CREATE_POSTALADDRESS',
  UPDATE_POSTALADDRESS: 'postalAddress/UPDATE_POSTALADDRESS',
  DELETE_POSTALADDRESS: 'postalAddress/DELETE_POSTALADDRESS',
  RESET: 'postalAddress/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPostalAddress>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PostalAddressState = Readonly<typeof initialState>;

// Reducer

export default (state: PostalAddressState = initialState, action): PostalAddressState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_POSTALADDRESS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_POSTALADDRESS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_POSTALADDRESS):
    case REQUEST(ACTION_TYPES.UPDATE_POSTALADDRESS):
    case REQUEST(ACTION_TYPES.DELETE_POSTALADDRESS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_POSTALADDRESS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_POSTALADDRESS):
    case FAILURE(ACTION_TYPES.CREATE_POSTALADDRESS):
    case FAILURE(ACTION_TYPES.UPDATE_POSTALADDRESS):
    case FAILURE(ACTION_TYPES.DELETE_POSTALADDRESS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_POSTALADDRESS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_POSTALADDRESS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_POSTALADDRESS):
    case SUCCESS(ACTION_TYPES.UPDATE_POSTALADDRESS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_POSTALADDRESS):
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

const apiUrl = SERVICENET_API_URL + '/postal-addresses';

// Actions

export const getEntities: ICrudGetAllAction<IPostalAddress> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_POSTALADDRESS_LIST,
    payload: axios.get<IPostalAddress>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPostalAddress> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_POSTALADDRESS,
    payload: axios.get<IPostalAddress>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPostalAddress> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_POSTALADDRESS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPostalAddress> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_POSTALADDRESS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPostalAddress> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_POSTALADDRESS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
