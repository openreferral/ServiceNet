import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPhysicalAddress, defaultValue } from 'app/shared/model/physical-address.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_PHYSICALADDRESS_LIST: 'physicalAddress/FETCH_PHYSICALADDRESS_LIST',
  FETCH_PHYSICALADDRESS: 'physicalAddress/FETCH_PHYSICALADDRESS',
  CREATE_PHYSICALADDRESS: 'physicalAddress/CREATE_PHYSICALADDRESS',
  UPDATE_PHYSICALADDRESS: 'physicalAddress/UPDATE_PHYSICALADDRESS',
  DELETE_PHYSICALADDRESS: 'physicalAddress/DELETE_PHYSICALADDRESS',
  RESET: 'physicalAddress/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPhysicalAddress>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type PhysicalAddressState = Readonly<typeof initialState>;

// Reducer

export default (state: PhysicalAddressState = initialState, action): PhysicalAddressState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PHYSICALADDRESS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PHYSICALADDRESS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PHYSICALADDRESS):
    case REQUEST(ACTION_TYPES.UPDATE_PHYSICALADDRESS):
    case REQUEST(ACTION_TYPES.DELETE_PHYSICALADDRESS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PHYSICALADDRESS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PHYSICALADDRESS):
    case FAILURE(ACTION_TYPES.CREATE_PHYSICALADDRESS):
    case FAILURE(ACTION_TYPES.UPDATE_PHYSICALADDRESS):
    case FAILURE(ACTION_TYPES.DELETE_PHYSICALADDRESS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PHYSICALADDRESS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_PHYSICALADDRESS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PHYSICALADDRESS):
    case SUCCESS(ACTION_TYPES.UPDATE_PHYSICALADDRESS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PHYSICALADDRESS):
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

const apiUrl = SERVICENET_API_URL + '/physical-addresses';

// Actions

export const getEntities: ICrudGetAllAction<IPhysicalAddress> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PHYSICALADDRESS_LIST,
    payload: axios.get<IPhysicalAddress>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IPhysicalAddress> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PHYSICALADDRESS,
    payload: axios.get<IPhysicalAddress>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPhysicalAddress> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PHYSICALADDRESS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPhysicalAddress> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PHYSICALADDRESS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPhysicalAddress> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PHYSICALADDRESS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
