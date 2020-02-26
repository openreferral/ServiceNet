import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IHolidaySchedule, defaultValue } from 'app/shared/model/holiday-schedule.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_HOLIDAYSCHEDULE_LIST: 'holidaySchedule/FETCH_HOLIDAYSCHEDULE_LIST',
  FETCH_HOLIDAYSCHEDULE: 'holidaySchedule/FETCH_HOLIDAYSCHEDULE',
  CREATE_HOLIDAYSCHEDULE: 'holidaySchedule/CREATE_HOLIDAYSCHEDULE',
  UPDATE_HOLIDAYSCHEDULE: 'holidaySchedule/UPDATE_HOLIDAYSCHEDULE',
  DELETE_HOLIDAYSCHEDULE: 'holidaySchedule/DELETE_HOLIDAYSCHEDULE',
  RESET: 'holidaySchedule/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHolidaySchedule>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type HolidayScheduleState = Readonly<typeof initialState>;

// Reducer

export default (state: HolidayScheduleState = initialState, action): HolidayScheduleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_HOLIDAYSCHEDULE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HOLIDAYSCHEDULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_HOLIDAYSCHEDULE):
    case REQUEST(ACTION_TYPES.UPDATE_HOLIDAYSCHEDULE):
    case REQUEST(ACTION_TYPES.DELETE_HOLIDAYSCHEDULE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_HOLIDAYSCHEDULE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HOLIDAYSCHEDULE):
    case FAILURE(ACTION_TYPES.CREATE_HOLIDAYSCHEDULE):
    case FAILURE(ACTION_TYPES.UPDATE_HOLIDAYSCHEDULE):
    case FAILURE(ACTION_TYPES.DELETE_HOLIDAYSCHEDULE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_HOLIDAYSCHEDULE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_HOLIDAYSCHEDULE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_HOLIDAYSCHEDULE):
    case SUCCESS(ACTION_TYPES.UPDATE_HOLIDAYSCHEDULE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_HOLIDAYSCHEDULE):
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

const apiUrl = SERVICENET_API_URL + '/holiday-schedules';

// Actions

export const getEntities: ICrudGetAllAction<IHolidaySchedule> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HOLIDAYSCHEDULE_LIST,
    payload: axios.get<IHolidaySchedule>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IHolidaySchedule> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HOLIDAYSCHEDULE,
    payload: axios.get<IHolidaySchedule>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IHolidaySchedule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HOLIDAYSCHEDULE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IHolidaySchedule> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HOLIDAYSCHEDULE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHolidaySchedule> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HOLIDAYSCHEDULE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
