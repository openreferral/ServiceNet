import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IEligibility, defaultValue } from 'app/shared/model/eligibility.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_ELIGIBILITY_LIST: 'eligibility/FETCH_ELIGIBILITY_LIST',
  FETCH_ELIGIBILITY: 'eligibility/FETCH_ELIGIBILITY',
  CREATE_ELIGIBILITY: 'eligibility/CREATE_ELIGIBILITY',
  UPDATE_ELIGIBILITY: 'eligibility/UPDATE_ELIGIBILITY',
  DELETE_ELIGIBILITY: 'eligibility/DELETE_ELIGIBILITY',
  RESET: 'eligibility/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IEligibility>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type EligibilityState = Readonly<typeof initialState>;

// Reducer

export default (state: EligibilityState = initialState, action): EligibilityState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ELIGIBILITY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ELIGIBILITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ELIGIBILITY):
    case REQUEST(ACTION_TYPES.UPDATE_ELIGIBILITY):
    case REQUEST(ACTION_TYPES.DELETE_ELIGIBILITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ELIGIBILITY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ELIGIBILITY):
    case FAILURE(ACTION_TYPES.CREATE_ELIGIBILITY):
    case FAILURE(ACTION_TYPES.UPDATE_ELIGIBILITY):
    case FAILURE(ACTION_TYPES.DELETE_ELIGIBILITY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ELIGIBILITY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_ELIGIBILITY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ELIGIBILITY):
    case SUCCESS(ACTION_TYPES.UPDATE_ELIGIBILITY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ELIGIBILITY):
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

const apiUrl = SERVICENET_API_URL + '/eligibilities';

// Actions

export const getEntities: ICrudGetAllAction<IEligibility> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ELIGIBILITY_LIST,
    payload: axios.get<IEligibility>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IEligibility> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ELIGIBILITY,
    payload: axios.get<IEligibility>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IEligibility> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ELIGIBILITY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IEligibility> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ELIGIBILITY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IEligibility> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ELIGIBILITY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
