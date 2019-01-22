import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IConfidentialRecord, defaultValue } from 'app/shared/model/confidential-record.model';

export const ACTION_TYPES = {
  FETCH_CONFIDENTIALRECORD_LIST: 'confidentialRecord/FETCH_CONFIDENTIALRECORD_LIST',
  FETCH_CONFIDENTIALRECORD: 'confidentialRecord/FETCH_CONFIDENTIALRECORD',
  CREATE_CONFIDENTIALRECORD: 'confidentialRecord/CREATE_CONFIDENTIALRECORD',
  UPDATE_CONFIDENTIALRECORD: 'confidentialRecord/UPDATE_CONFIDENTIALRECORD',
  DELETE_CONFIDENTIALRECORD: 'confidentialRecord/DELETE_CONFIDENTIALRECORD',
  RESET: 'confidentialRecord/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IConfidentialRecord>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ConfidentialRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: ConfidentialRecordState = initialState, action): ConfidentialRecordState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CONFIDENTIALRECORD_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CONFIDENTIALRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CONFIDENTIALRECORD):
    case REQUEST(ACTION_TYPES.UPDATE_CONFIDENTIALRECORD):
    case REQUEST(ACTION_TYPES.DELETE_CONFIDENTIALRECORD):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_CONFIDENTIALRECORD_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CONFIDENTIALRECORD):
    case FAILURE(ACTION_TYPES.CREATE_CONFIDENTIALRECORD):
    case FAILURE(ACTION_TYPES.UPDATE_CONFIDENTIALRECORD):
    case FAILURE(ACTION_TYPES.DELETE_CONFIDENTIALRECORD):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONFIDENTIALRECORD_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CONFIDENTIALRECORD):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CONFIDENTIALRECORD):
    case SUCCESS(ACTION_TYPES.UPDATE_CONFIDENTIALRECORD):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CONFIDENTIALRECORD):
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

const apiUrl = 'api/confidential-records';

// Actions

export const getEntities: ICrudGetAllAction<IConfidentialRecord> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_CONFIDENTIALRECORD_LIST,
  payload: axios.get<IConfidentialRecord>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IConfidentialRecord> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CONFIDENTIALRECORD,
    payload: axios.get<IConfidentialRecord>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IConfidentialRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CONFIDENTIALRECORD,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IConfidentialRecord> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CONFIDENTIALRECORD,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IConfidentialRecord> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CONFIDENTIALRECORD,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
