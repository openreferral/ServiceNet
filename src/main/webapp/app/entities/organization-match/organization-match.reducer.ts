import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOrganizationMatch, defaultValue } from 'app/shared/model/organization-match.model';

export const ACTION_TYPES = {
  FETCH_ORGANIZATIONMATCH_LIST: 'organizationMatch/FETCH_ORGANIZATIONMATCH_LIST',
  FETCH_ORGANIZATIONMATCH: 'organizationMatch/FETCH_ORGANIZATIONMATCH',
  CREATE_ORGANIZATIONMATCH: 'organizationMatch/CREATE_ORGANIZATIONMATCH',
  UPDATE_ORGANIZATIONMATCH: 'organizationMatch/UPDATE_ORGANIZATIONMATCH',
  DELETE_ORGANIZATIONMATCH: 'organizationMatch/DELETE_ORGANIZATIONMATCH',
  SET_BLOB: 'organizationMatch/SET_BLOB',
  RESET: 'organizationMatch/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOrganizationMatch>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type OrganizationMatchState = Readonly<typeof initialState>;

// Reducer

export default (state: OrganizationMatchState = initialState, action): OrganizationMatchState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ORGANIZATIONMATCH_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ORGANIZATIONMATCH):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ORGANIZATIONMATCH):
    case REQUEST(ACTION_TYPES.UPDATE_ORGANIZATIONMATCH):
    case REQUEST(ACTION_TYPES.DELETE_ORGANIZATIONMATCH):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ORGANIZATIONMATCH_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ORGANIZATIONMATCH):
    case FAILURE(ACTION_TYPES.CREATE_ORGANIZATIONMATCH):
    case FAILURE(ACTION_TYPES.UPDATE_ORGANIZATIONMATCH):
    case FAILURE(ACTION_TYPES.DELETE_ORGANIZATIONMATCH):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORGANIZATIONMATCH_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ORGANIZATIONMATCH):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ORGANIZATIONMATCH):
    case SUCCESS(ACTION_TYPES.UPDATE_ORGANIZATIONMATCH):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ORGANIZATIONMATCH):
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

const apiUrl = 'api/organization-matches';

// Actions

export const getEntities: ICrudGetAllAction<IOrganizationMatch> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ORGANIZATIONMATCH_LIST,
  payload: axios.get<IOrganizationMatch>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IOrganizationMatch> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ORGANIZATIONMATCH,
    payload: axios.get<IOrganizationMatch>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOrganizationMatch> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ORGANIZATIONMATCH,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOrganizationMatch> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ORGANIZATIONMATCH,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOrganizationMatch> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ORGANIZATIONMATCH,
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
