import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRequiredDocument, defaultValue } from 'app/shared/model/required-document.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_REQUIREDDOCUMENT_LIST: 'requiredDocument/FETCH_REQUIREDDOCUMENT_LIST',
  FETCH_REQUIREDDOCUMENT: 'requiredDocument/FETCH_REQUIREDDOCUMENT',
  CREATE_REQUIREDDOCUMENT: 'requiredDocument/CREATE_REQUIREDDOCUMENT',
  UPDATE_REQUIREDDOCUMENT: 'requiredDocument/UPDATE_REQUIREDDOCUMENT',
  DELETE_REQUIREDDOCUMENT: 'requiredDocument/DELETE_REQUIREDDOCUMENT',
  RESET: 'requiredDocument/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRequiredDocument>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type RequiredDocumentState = Readonly<typeof initialState>;

// Reducer

export default (state: RequiredDocumentState = initialState, action): RequiredDocumentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_REQUIREDDOCUMENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_REQUIREDDOCUMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_REQUIREDDOCUMENT):
    case REQUEST(ACTION_TYPES.UPDATE_REQUIREDDOCUMENT):
    case REQUEST(ACTION_TYPES.DELETE_REQUIREDDOCUMENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_REQUIREDDOCUMENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_REQUIREDDOCUMENT):
    case FAILURE(ACTION_TYPES.CREATE_REQUIREDDOCUMENT):
    case FAILURE(ACTION_TYPES.UPDATE_REQUIREDDOCUMENT):
    case FAILURE(ACTION_TYPES.DELETE_REQUIREDDOCUMENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_REQUIREDDOCUMENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_REQUIREDDOCUMENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_REQUIREDDOCUMENT):
    case SUCCESS(ACTION_TYPES.UPDATE_REQUIREDDOCUMENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_REQUIREDDOCUMENT):
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

const apiUrl = SERVICENET_API_URL + '/required-documents';

// Actions

export const getEntities: ICrudGetAllAction<IRequiredDocument> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_REQUIREDDOCUMENT_LIST,
    payload: axios.get<IRequiredDocument>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRequiredDocument> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_REQUIREDDOCUMENT,
    payload: axios.get<IRequiredDocument>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRequiredDocument> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_REQUIREDDOCUMENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRequiredDocument> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_REQUIREDDOCUMENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRequiredDocument> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_REQUIREDDOCUMENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
