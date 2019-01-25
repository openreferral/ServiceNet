import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFieldExclusion, defaultValue } from 'app/shared/model/field-exclusion.model';

export const ACTION_TYPES = {
  FETCH_FIELDEXCLUSION_LIST: 'fieldExclusion/FETCH_FIELDEXCLUSION_LIST',
  FETCH_FIELDEXCLUSION: 'fieldExclusion/FETCH_FIELDEXCLUSION',
  CREATE_FIELDEXCLUSION: 'fieldExclusion/CREATE_FIELDEXCLUSION',
  UPDATE_FIELDEXCLUSION: 'fieldExclusion/UPDATE_FIELDEXCLUSION',
  DELETE_FIELDEXCLUSION: 'fieldExclusion/DELETE_FIELDEXCLUSION',
  RESET: 'fieldExclusion/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFieldExclusion>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type FieldExclusionState = Readonly<typeof initialState>;

// Reducer

export default (state: FieldExclusionState = initialState, action): FieldExclusionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FIELDEXCLUSION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FIELDEXCLUSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FIELDEXCLUSION):
    case REQUEST(ACTION_TYPES.UPDATE_FIELDEXCLUSION):
    case REQUEST(ACTION_TYPES.DELETE_FIELDEXCLUSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_FIELDEXCLUSION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FIELDEXCLUSION):
    case FAILURE(ACTION_TYPES.CREATE_FIELDEXCLUSION):
    case FAILURE(ACTION_TYPES.UPDATE_FIELDEXCLUSION):
    case FAILURE(ACTION_TYPES.DELETE_FIELDEXCLUSION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_FIELDEXCLUSION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_FIELDEXCLUSION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FIELDEXCLUSION):
    case SUCCESS(ACTION_TYPES.UPDATE_FIELDEXCLUSION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FIELDEXCLUSION):
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

const apiUrl = 'api/field-exclusions';

// Actions

export const getEntities: ICrudGetAllAction<IFieldExclusion> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_FIELDEXCLUSION_LIST,
  payload: axios.get<IFieldExclusion>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IFieldExclusion> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FIELDEXCLUSION,
    payload: axios.get<IFieldExclusion>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFieldExclusion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FIELDEXCLUSION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFieldExclusion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FIELDEXCLUSION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFieldExclusion> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FIELDEXCLUSION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
