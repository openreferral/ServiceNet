import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IExclusionsConfig, defaultValue } from 'app/shared/model/exclusions-config.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_EXCLUSIONSCONFIG_LIST: 'exclusionsConfig/FETCH_EXCLUSIONSCONFIG_LIST',
  FETCH_EXCLUSIONSCONFIG: 'exclusionsConfig/FETCH_EXCLUSIONSCONFIG',
  CREATE_EXCLUSIONSCONFIG: 'exclusionsConfig/CREATE_EXCLUSIONSCONFIG',
  UPDATE_EXCLUSIONSCONFIG: 'exclusionsConfig/UPDATE_EXCLUSIONSCONFIG',
  DELETE_EXCLUSIONSCONFIG: 'exclusionsConfig/DELETE_EXCLUSIONSCONFIG',
  RESET: 'exclusionsConfig/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IExclusionsConfig>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ExclusionsConfigState = Readonly<typeof initialState>;

// Reducer

export default (state: ExclusionsConfigState = initialState, action): ExclusionsConfigState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_EXCLUSIONSCONFIG_LIST):
    case REQUEST(ACTION_TYPES.FETCH_EXCLUSIONSCONFIG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_EXCLUSIONSCONFIG):
    case REQUEST(ACTION_TYPES.UPDATE_EXCLUSIONSCONFIG):
    case REQUEST(ACTION_TYPES.DELETE_EXCLUSIONSCONFIG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_EXCLUSIONSCONFIG_LIST):
    case FAILURE(ACTION_TYPES.FETCH_EXCLUSIONSCONFIG):
    case FAILURE(ACTION_TYPES.CREATE_EXCLUSIONSCONFIG):
    case FAILURE(ACTION_TYPES.UPDATE_EXCLUSIONSCONFIG):
    case FAILURE(ACTION_TYPES.DELETE_EXCLUSIONSCONFIG):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_EXCLUSIONSCONFIG_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_EXCLUSIONSCONFIG):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_EXCLUSIONSCONFIG):
    case SUCCESS(ACTION_TYPES.UPDATE_EXCLUSIONSCONFIG):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_EXCLUSIONSCONFIG):
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

const apiUrl = SERVICENET_API_URL + '/exclusions-configs';

// Actions

export const getEntities: ICrudGetAllAction<IExclusionsConfig> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_EXCLUSIONSCONFIG_LIST,
  payload: axios.get<IExclusionsConfig>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IExclusionsConfig> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_EXCLUSIONSCONFIG,
    payload: axios.get<IExclusionsConfig>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IExclusionsConfig> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_EXCLUSIONSCONFIG,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IExclusionsConfig> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_EXCLUSIONSCONFIG,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IExclusionsConfig> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_EXCLUSIONSCONFIG,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
