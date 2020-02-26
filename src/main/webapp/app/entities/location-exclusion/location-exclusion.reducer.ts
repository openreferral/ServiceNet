import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ILocationExclusion, defaultValue } from 'app/shared/model/location-exclusion.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_LOCATIONEXCLUSION_LIST: 'locationExclusion/FETCH_LOCATIONEXCLUSION_LIST',
  FETCH_LOCATIONEXCLUSION: 'locationExclusion/FETCH_LOCATIONEXCLUSION',
  CREATE_LOCATIONEXCLUSION: 'locationExclusion/CREATE_LOCATIONEXCLUSION',
  UPDATE_LOCATIONEXCLUSION: 'locationExclusion/UPDATE_LOCATIONEXCLUSION',
  DELETE_LOCATIONEXCLUSION: 'locationExclusion/DELETE_LOCATIONEXCLUSION',
  RESET: 'locationExclusion/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ILocationExclusion>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type LocationExclusionState = Readonly<typeof initialState>;

// Reducer

export default (state: LocationExclusionState = initialState, action): LocationExclusionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_LOCATIONEXCLUSION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LOCATIONEXCLUSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_LOCATIONEXCLUSION):
    case REQUEST(ACTION_TYPES.UPDATE_LOCATIONEXCLUSION):
    case REQUEST(ACTION_TYPES.DELETE_LOCATIONEXCLUSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_LOCATIONEXCLUSION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LOCATIONEXCLUSION):
    case FAILURE(ACTION_TYPES.CREATE_LOCATIONEXCLUSION):
    case FAILURE(ACTION_TYPES.UPDATE_LOCATIONEXCLUSION):
    case FAILURE(ACTION_TYPES.DELETE_LOCATIONEXCLUSION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOCATIONEXCLUSION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_LOCATIONEXCLUSION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_LOCATIONEXCLUSION):
    case SUCCESS(ACTION_TYPES.UPDATE_LOCATIONEXCLUSION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_LOCATIONEXCLUSION):
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

const apiUrl = SERVICENET_API_URL + '/location-exclusions';

// Actions

export const getEntities: ICrudGetAllAction<ILocationExclusion> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_LOCATIONEXCLUSION_LIST,
  payload: axios.get<ILocationExclusion>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ILocationExclusion> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_LOCATIONEXCLUSION,
    payload: axios.get<ILocationExclusion>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ILocationExclusion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LOCATIONEXCLUSION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ILocationExclusion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_LOCATIONEXCLUSION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ILocationExclusion> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LOCATIONEXCLUSION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
