import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDocumentUpload, defaultValue } from 'app/shared/model/document-upload.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_DOCUMENTUPLOAD_LIST: 'documentUpload/FETCH_DOCUMENTUPLOAD_LIST',
  FETCH_DOCUMENTUPLOAD: 'documentUpload/FETCH_DOCUMENTUPLOAD',
  CREATE_DOCUMENTUPLOAD: 'documentUpload/CREATE_DOCUMENTUPLOAD',
  UPDATE_DOCUMENTUPLOAD: 'documentUpload/UPDATE_DOCUMENTUPLOAD',
  DELETE_DOCUMENTUPLOAD: 'documentUpload/DELETE_DOCUMENTUPLOAD',
  RESET: 'documentUpload/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDocumentUpload>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DocumentUploadState = Readonly<typeof initialState>;

// Reducer

export default (state: DocumentUploadState = initialState, action): DocumentUploadState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DOCUMENTUPLOAD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DOCUMENTUPLOAD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DOCUMENTUPLOAD):
    case REQUEST(ACTION_TYPES.UPDATE_DOCUMENTUPLOAD):
    case REQUEST(ACTION_TYPES.DELETE_DOCUMENTUPLOAD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DOCUMENTUPLOAD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DOCUMENTUPLOAD):
    case FAILURE(ACTION_TYPES.CREATE_DOCUMENTUPLOAD):
    case FAILURE(ACTION_TYPES.UPDATE_DOCUMENTUPLOAD):
    case FAILURE(ACTION_TYPES.DELETE_DOCUMENTUPLOAD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DOCUMENTUPLOAD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_DOCUMENTUPLOAD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DOCUMENTUPLOAD):
    case SUCCESS(ACTION_TYPES.UPDATE_DOCUMENTUPLOAD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DOCUMENTUPLOAD):
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

const apiUrl = SERVICENET_API_URL + '/document-uploads';

// Actions

export const getEntities: ICrudGetAllAction<IDocumentUpload> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DOCUMENTUPLOAD_LIST,
    payload: axios.get<IDocumentUpload>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDocumentUpload> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DOCUMENTUPLOAD,
    payload: axios.get<IDocumentUpload>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDocumentUpload> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DOCUMENTUPLOAD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDocumentUpload> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DOCUMENTUPLOAD,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDocumentUpload> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DOCUMENTUPLOAD,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
