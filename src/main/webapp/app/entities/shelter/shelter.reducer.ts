import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShelter, defaultValue } from 'app/shared/model/shelter.model';

export const ACTION_TYPES = {
  FETCH_SHELTER_LIST: 'shelter/FETCH_SHELTER_LIST',
  FETCH_SHELTER: 'shelter/FETCH_SHELTER',
  CREATE_SHELTER: 'shelter/CREATE_SHELTER',
  UPDATE_SHELTER: 'shelter/UPDATE_SHELTER',
  DELETE_SHELTER: 'shelter/DELETE_SHELTER',
  RESET: 'shelter/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShelter>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ShelterState = Readonly<typeof initialState>;

// Reducer

export default (state: ShelterState = initialState, action): ShelterState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SHELTER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHELTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHELTER):
    case REQUEST(ACTION_TYPES.UPDATE_SHELTER):
    case REQUEST(ACTION_TYPES.DELETE_SHELTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SHELTER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHELTER):
    case FAILURE(ACTION_TYPES.CREATE_SHELTER):
    case FAILURE(ACTION_TYPES.UPDATE_SHELTER):
    case FAILURE(ACTION_TYPES.DELETE_SHELTER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHELTER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHELTER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHELTER):
    case SUCCESS(ACTION_TYPES.UPDATE_SHELTER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHELTER):
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

const apiUrl = 'api/shelters';

// Actions

export const getEntities: ICrudGetAllAction<IShelter> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SHELTER_LIST,
  payload: axios.get<IShelter>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IShelter> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHELTER,
    payload: axios.get<IShelter>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShelter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHELTER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShelter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHELTER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShelter> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHELTER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
