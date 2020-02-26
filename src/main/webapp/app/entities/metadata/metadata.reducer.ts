import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMetadata, defaultValue } from 'app/shared/model/metadata.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_METADATA_LIST: 'metadata/FETCH_METADATA_LIST',
  FETCH_METADATA: 'metadata/FETCH_METADATA',
  CREATE_METADATA: 'metadata/CREATE_METADATA',
  UPDATE_METADATA: 'metadata/UPDATE_METADATA',
  DELETE_METADATA: 'metadata/DELETE_METADATA',
  SET_BLOB: 'metadata/SET_BLOB',
  RESET: 'metadata/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMetadata>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type MetadataState = Readonly<typeof initialState>;

// Reducer

export default (state: MetadataState = initialState, action): MetadataState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_METADATA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_METADATA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_METADATA):
    case REQUEST(ACTION_TYPES.UPDATE_METADATA):
    case REQUEST(ACTION_TYPES.DELETE_METADATA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_METADATA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_METADATA):
    case FAILURE(ACTION_TYPES.CREATE_METADATA):
    case FAILURE(ACTION_TYPES.UPDATE_METADATA):
    case FAILURE(ACTION_TYPES.DELETE_METADATA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_METADATA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_METADATA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_METADATA):
    case SUCCESS(ACTION_TYPES.UPDATE_METADATA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_METADATA):
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

const apiUrl = SERVICENET_API_URL + '/metadata';

// Actions

export const getEntities: ICrudGetAllAction<IMetadata> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_METADATA_LIST,
  payload: axios.get<IMetadata>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IMetadata> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_METADATA,
    payload: axios.get<IMetadata>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMetadata> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_METADATA,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMetadata> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_METADATA,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMetadata> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_METADATA,
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
