import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOption, defaultValue } from 'app/shared/model/option.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_OPTION_LIST: 'option/FETCH_OPTION_LIST',
  FETCH_LANGUAGES: 'option/FETCH_LANGUAGES',
  FETCH_DEFINED_COVERAGE_AREAS: 'option/FETCH_DEFINED_COVERAGE_AREAS',
  FETCH_TAGS: 'option/FETCH_TAGS',
  FETCH_OPTION: 'option/FETCH_OPTION',
  CREATE_OPTION: 'option/CREATE_OPTION',
  UPDATE_OPTION: 'option/UPDATE_OPTION',
  DELETE_OPTION: 'option/DELETE_OPTION',
  RESET: 'option/RESET'
};

export const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOption>,
  languages: [] as ReadonlyArray<IOption>,
  definedCoverageAreas: [] as ReadonlyArray<IOption>,
  tags: [] as ReadonlyArray<IOption>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type OptionState = Readonly<typeof initialState>;

// Reducer

export default (state: OptionState = initialState, action): OptionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_OPTION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_LANGUAGES):
    case REQUEST(ACTION_TYPES.FETCH_DEFINED_COVERAGE_AREAS):
    case REQUEST(ACTION_TYPES.FETCH_TAGS):
    case REQUEST(ACTION_TYPES.FETCH_OPTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_OPTION):
    case REQUEST(ACTION_TYPES.UPDATE_OPTION):
    case REQUEST(ACTION_TYPES.DELETE_OPTION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_OPTION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_LANGUAGES):
    case FAILURE(ACTION_TYPES.FETCH_DEFINED_COVERAGE_AREAS):
    case FAILURE(ACTION_TYPES.FETCH_TAGS):
    case FAILURE(ACTION_TYPES.FETCH_OPTION):
    case FAILURE(ACTION_TYPES.CREATE_OPTION):
    case FAILURE(ACTION_TYPES.UPDATE_OPTION):
    case FAILURE(ACTION_TYPES.DELETE_OPTION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_OPTION_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_LANGUAGES):
      return {
        ...state,
        loading: false,
        languages: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DEFINED_COVERAGE_AREAS):
      return {
        ...state,
        loading: false,
        definedCoverageAreas: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TAGS):
      return {
        ...state,
        loading: false,
        tags: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_OPTION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_OPTION):
    case SUCCESS(ACTION_TYPES.UPDATE_OPTION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_OPTION):
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

const apiUrl = SERVICENET_API_URL + '/options';

// Actions

export const getEntities: ICrudGetAllAction<IOption> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${`?page=${page}&size=${size}&sort=${sort}`}`;
  return {
    type: ACTION_TYPES.FETCH_OPTION_LIST,
    payload: axios.get<IOption>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getLanguages: ICrudGetAllAction<IOption> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_LANGUAGES,
  payload: axios.get<IOption>(`${apiUrl}/search/?type=LANGUAGE&cacheBuster=${new Date().getTime()}`)
});

export const getDefinedCoverageAreas: ICrudGetAllAction<IOption> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_DEFINED_COVERAGE_AREAS,
  payload: axios.get<IOption>(`${apiUrl}/search/?type=DEFINED_COVERAGE_AREA&cacheBuster=${new Date().getTime()}`)
});

export const getTags: ICrudGetAllAction<IOption> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TAGS,
  payload: axios.get<IOption>(`${apiUrl}/search/?type=TAG&cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IOption> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_OPTION,
    payload: axios.get<IOption>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IOption> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_OPTION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOption> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_OPTION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOption> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_OPTION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
