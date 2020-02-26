import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IActivityFilter, defaultValue } from 'app/shared/model/activity-filter.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_ACTIVITYFILTER_LIST: 'activityFilter/FETCH_ACTIVITYFILTER_LIST',
  FETCH_ACTIVITYFILTER: 'activityFilter/FETCH_ACTIVITYFILTER',
  CREATE_ACTIVITYFILTER: 'activityFilter/CREATE_ACTIVITYFILTER',
  UPDATE_ACTIVITYFILTER: 'activityFilter/UPDATE_ACTIVITYFILTER',
  DELETE_ACTIVITYFILTER: 'activityFilter/DELETE_ACTIVITYFILTER',
  RESET: 'activityFilter/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IActivityFilter>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ActivityFilterState = Readonly<typeof initialState>;

// Reducer

export default (state: ActivityFilterState = initialState, action): ActivityFilterState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ACTIVITYFILTER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACTIVITYFILTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ACTIVITYFILTER):
    case REQUEST(ACTION_TYPES.UPDATE_ACTIVITYFILTER):
    case REQUEST(ACTION_TYPES.DELETE_ACTIVITYFILTER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ACTIVITYFILTER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACTIVITYFILTER):
    case FAILURE(ACTION_TYPES.CREATE_ACTIVITYFILTER):
    case FAILURE(ACTION_TYPES.UPDATE_ACTIVITYFILTER):
    case FAILURE(ACTION_TYPES.DELETE_ACTIVITYFILTER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVITYFILTER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVITYFILTER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACTIVITYFILTER):
    case SUCCESS(ACTION_TYPES.UPDATE_ACTIVITYFILTER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACTIVITYFILTER):
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

const apiUrl = SERVICENET_API_URL + '/activity-filters';

// Actions

export const getEntities: ICrudGetAllAction<IActivityFilter> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVITYFILTER_LIST,
    payload: axios.get<IActivityFilter>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IActivityFilter> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVITYFILTER,
    payload: axios.get<IActivityFilter>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IActivityFilter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACTIVITYFILTER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IActivityFilter> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACTIVITYFILTER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IActivityFilter> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACTIVITYFILTER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
