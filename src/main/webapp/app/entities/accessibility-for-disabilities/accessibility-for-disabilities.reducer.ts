import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAccessibilityForDisabilities, defaultValue } from 'app/shared/model/accessibility-for-disabilities.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_ACCESSIBILITYFORDISABILITIES_LIST: 'accessibilityForDisabilities/FETCH_ACCESSIBILITYFORDISABILITIES_LIST',
  FETCH_ACCESSIBILITYFORDISABILITIES: 'accessibilityForDisabilities/FETCH_ACCESSIBILITYFORDISABILITIES',
  CREATE_ACCESSIBILITYFORDISABILITIES: 'accessibilityForDisabilities/CREATE_ACCESSIBILITYFORDISABILITIES',
  UPDATE_ACCESSIBILITYFORDISABILITIES: 'accessibilityForDisabilities/UPDATE_ACCESSIBILITYFORDISABILITIES',
  DELETE_ACCESSIBILITYFORDISABILITIES: 'accessibilityForDisabilities/DELETE_ACCESSIBILITYFORDISABILITIES',
  RESET: 'accessibilityForDisabilities/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAccessibilityForDisabilities>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type AccessibilityForDisabilitiesState = Readonly<typeof initialState>;

// Reducer

export default (state: AccessibilityForDisabilitiesState = initialState, action): AccessibilityForDisabilitiesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ACCESSIBILITYFORDISABILITIES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACCESSIBILITYFORDISABILITIES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ACCESSIBILITYFORDISABILITIES):
    case REQUEST(ACTION_TYPES.UPDATE_ACCESSIBILITYFORDISABILITIES):
    case REQUEST(ACTION_TYPES.DELETE_ACCESSIBILITYFORDISABILITIES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ACCESSIBILITYFORDISABILITIES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACCESSIBILITYFORDISABILITIES):
    case FAILURE(ACTION_TYPES.CREATE_ACCESSIBILITYFORDISABILITIES):
    case FAILURE(ACTION_TYPES.UPDATE_ACCESSIBILITYFORDISABILITIES):
    case FAILURE(ACTION_TYPES.DELETE_ACCESSIBILITYFORDISABILITIES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACCESSIBILITYFORDISABILITIES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACCESSIBILITYFORDISABILITIES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACCESSIBILITYFORDISABILITIES):
    case SUCCESS(ACTION_TYPES.UPDATE_ACCESSIBILITYFORDISABILITIES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACCESSIBILITYFORDISABILITIES):
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

const apiUrl = SERVICENET_API_URL + '/accessibility-for-disabilities';

// Actions

export const getEntities: ICrudGetAllAction<IAccessibilityForDisabilities> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ACCESSIBILITYFORDISABILITIES_LIST,
    payload: axios.get<IAccessibilityForDisabilities>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IAccessibilityForDisabilities> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACCESSIBILITYFORDISABILITIES,
    payload: axios.get<IAccessibilityForDisabilities>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAccessibilityForDisabilities> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACCESSIBILITYFORDISABILITIES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAccessibilityForDisabilities> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACCESSIBILITYFORDISABILITIES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAccessibilityForDisabilities> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACCESSIBILITYFORDISABILITIES,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
