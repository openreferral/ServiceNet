import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRegularSchedule, defaultValue } from 'app/shared/model/regular-schedule.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_REGULARSCHEDULE_LIST: 'regularSchedule/FETCH_REGULARSCHEDULE_LIST',
  FETCH_REGULARSCHEDULE: 'regularSchedule/FETCH_REGULARSCHEDULE',
  CREATE_REGULARSCHEDULE: 'regularSchedule/CREATE_REGULARSCHEDULE',
  UPDATE_REGULARSCHEDULE: 'regularSchedule/UPDATE_REGULARSCHEDULE',
  DELETE_REGULARSCHEDULE: 'regularSchedule/DELETE_REGULARSCHEDULE',
  RESET: 'regularSchedule/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRegularSchedule>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type RegularScheduleState = Readonly<typeof initialState>;

// Reducer

export default (state: RegularScheduleState = initialState, action): RegularScheduleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_REGULARSCHEDULE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_REGULARSCHEDULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_REGULARSCHEDULE):
    case REQUEST(ACTION_TYPES.UPDATE_REGULARSCHEDULE):
    case REQUEST(ACTION_TYPES.DELETE_REGULARSCHEDULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_REGULARSCHEDULE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_REGULARSCHEDULE):
    case FAILURE(ACTION_TYPES.CREATE_REGULARSCHEDULE):
    case FAILURE(ACTION_TYPES.UPDATE_REGULARSCHEDULE):
    case FAILURE(ACTION_TYPES.DELETE_REGULARSCHEDULE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_REGULARSCHEDULE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_REGULARSCHEDULE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_REGULARSCHEDULE):
    case SUCCESS(ACTION_TYPES.UPDATE_REGULARSCHEDULE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_REGULARSCHEDULE):
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

const apiUrl = SERVICENET_API_URL + '/regular-schedules';

// Actions

export const getEntities: ICrudGetAllAction<IRegularSchedule> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_REGULARSCHEDULE_LIST,
    payload: axios.get<IRegularSchedule>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRegularSchedule> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_REGULARSCHEDULE,
    payload: axios.get<IRegularSchedule>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRegularSchedule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_REGULARSCHEDULE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRegularSchedule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_REGULARSCHEDULE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRegularSchedule> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_REGULARSCHEDULE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
