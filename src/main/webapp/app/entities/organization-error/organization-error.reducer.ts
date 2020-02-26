import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOrganizationError, defaultValue } from 'app/shared/model/organization-error.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_ORGANIZATIONERROR_LIST: 'organizationError/FETCH_ORGANIZATIONERROR_LIST',
  FETCH_ORGANIZATIONERROR: 'organizationError/FETCH_ORGANIZATIONERROR',
  CREATE_ORGANIZATIONERROR: 'organizationError/CREATE_ORGANIZATIONERROR',
  UPDATE_ORGANIZATIONERROR: 'organizationError/UPDATE_ORGANIZATIONERROR',
  DELETE_ORGANIZATIONERROR: 'organizationError/DELETE_ORGANIZATIONERROR',
  RESET: 'organizationError/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOrganizationError>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OrganizationErrorState = Readonly<typeof initialState>;

// Reducer

export default (state: OrganizationErrorState = initialState, action): OrganizationErrorState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ORGANIZATIONERROR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ORGANIZATIONERROR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ORGANIZATIONERROR):
    case REQUEST(ACTION_TYPES.UPDATE_ORGANIZATIONERROR):
    case REQUEST(ACTION_TYPES.DELETE_ORGANIZATIONERROR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ORGANIZATIONERROR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ORGANIZATIONERROR):
    case FAILURE(ACTION_TYPES.CREATE_ORGANIZATIONERROR):
    case FAILURE(ACTION_TYPES.UPDATE_ORGANIZATIONERROR):
    case FAILURE(ACTION_TYPES.DELETE_ORGANIZATIONERROR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORGANIZATIONERROR_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORGANIZATIONERROR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ORGANIZATIONERROR):
    case SUCCESS(ACTION_TYPES.UPDATE_ORGANIZATIONERROR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ORGANIZATIONERROR):
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

const apiUrl = SERVICENET_API_URL + '/organization-errors';

// Actions

export const getEntities: ICrudGetAllAction<IOrganizationError> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ORGANIZATIONERROR_LIST,
    payload: axios.get<IOrganizationError>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IOrganizationError> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ORGANIZATIONERROR,
    payload: axios.get<IOrganizationError>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOrganizationError> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ORGANIZATIONERROR,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IOrganizationError> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ORGANIZATIONERROR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOrganizationError> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ORGANIZATIONERROR,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
