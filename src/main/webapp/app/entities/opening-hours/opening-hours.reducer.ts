import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOpeningHours, defaultValue } from 'app/shared/model/opening-hours.model';

export const ACTION_TYPES = {
  FETCH_OPENINGHOURS_LIST: 'openingHours/FETCH_OPENINGHOURS_LIST',
  FETCH_OPENINGHOURS: 'openingHours/FETCH_OPENINGHOURS',
  CREATE_OPENINGHOURS: 'openingHours/CREATE_OPENINGHOURS',
  UPDATE_OPENINGHOURS: 'openingHours/UPDATE_OPENINGHOURS',
  DELETE_OPENINGHOURS: 'openingHours/DELETE_OPENINGHOURS',
  RESET: 'openingHours/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOpeningHours>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type OpeningHoursState = Readonly<typeof initialState>;

// Reducer

export default (state: OpeningHoursState = initialState, action): OpeningHoursState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_OPENINGHOURS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_OPENINGHOURS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_OPENINGHOURS):
    case REQUEST(ACTION_TYPES.UPDATE_OPENINGHOURS):
    case REQUEST(ACTION_TYPES.DELETE_OPENINGHOURS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_OPENINGHOURS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_OPENINGHOURS):
    case FAILURE(ACTION_TYPES.CREATE_OPENINGHOURS):
    case FAILURE(ACTION_TYPES.UPDATE_OPENINGHOURS):
    case FAILURE(ACTION_TYPES.DELETE_OPENINGHOURS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_OPENINGHOURS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_OPENINGHOURS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_OPENINGHOURS):
    case SUCCESS(ACTION_TYPES.UPDATE_OPENINGHOURS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_OPENINGHOURS):
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

const apiUrl = 'api/opening-hours';

// Actions

export const getEntities: ICrudGetAllAction<IOpeningHours> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_OPENINGHOURS_LIST,
  payload: axios.get<IOpeningHours>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IOpeningHours> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_OPENINGHOURS,
    payload: axios.get<IOpeningHours>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOpeningHours> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_OPENINGHOURS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOpeningHours> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_OPENINGHOURS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOpeningHours> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_OPENINGHOURS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
