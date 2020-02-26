import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOrganization, defaultValue } from 'app/shared/model/organization.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_ORGANIZATION_LIST: 'organization/FETCH_ORGANIZATION_LIST',
  FETCH_ORGANIZATION: 'organization/FETCH_ORGANIZATION',
  CREATE_ORGANIZATION: 'organization/CREATE_ORGANIZATION',
  UPDATE_ORGANIZATION: 'organization/UPDATE_ORGANIZATION',
  DELETE_ORGANIZATION: 'organization/DELETE_ORGANIZATION',
  SET_BLOB: 'organization/SET_BLOB',
  RESET: 'organization/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOrganization>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OrganizationState = Readonly<typeof initialState>;

// Reducer

export default (state: OrganizationState = initialState, action): OrganizationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ORGANIZATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ORGANIZATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ORGANIZATION):
    case REQUEST(ACTION_TYPES.UPDATE_ORGANIZATION):
    case REQUEST(ACTION_TYPES.DELETE_ORGANIZATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ORGANIZATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ORGANIZATION):
    case FAILURE(ACTION_TYPES.CREATE_ORGANIZATION):
    case FAILURE(ACTION_TYPES.UPDATE_ORGANIZATION):
    case FAILURE(ACTION_TYPES.DELETE_ORGANIZATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORGANIZATION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORGANIZATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ORGANIZATION):
    case SUCCESS(ACTION_TYPES.UPDATE_ORGANIZATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ORGANIZATION):
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

const apiUrl = SERVICENET_API_URL + '/organizations';

// Actions

export const getEntities: ICrudGetAllAction<IOrganization> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ORGANIZATION_LIST,
    payload: axios.get<IOrganization>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IOrganization> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ORGANIZATION,
    payload: axios.get<IOrganization>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOrganization> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ORGANIZATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOrganization> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ORGANIZATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOrganization> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ORGANIZATION,
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
