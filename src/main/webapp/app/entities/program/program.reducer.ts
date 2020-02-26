import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProgram, defaultValue } from 'app/shared/model/program.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_PROGRAM_LIST: 'program/FETCH_PROGRAM_LIST',
  FETCH_PROGRAM: 'program/FETCH_PROGRAM',
  CREATE_PROGRAM: 'program/CREATE_PROGRAM',
  UPDATE_PROGRAM: 'program/UPDATE_PROGRAM',
  DELETE_PROGRAM: 'program/DELETE_PROGRAM',
  RESET: 'program/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProgram>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ProgramState = Readonly<typeof initialState>;

// Reducer

export default (state: ProgramState = initialState, action): ProgramState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PROGRAM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PROGRAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PROGRAM):
    case REQUEST(ACTION_TYPES.UPDATE_PROGRAM):
    case REQUEST(ACTION_TYPES.DELETE_PROGRAM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PROGRAM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PROGRAM):
    case FAILURE(ACTION_TYPES.CREATE_PROGRAM):
    case FAILURE(ACTION_TYPES.UPDATE_PROGRAM):
    case FAILURE(ACTION_TYPES.DELETE_PROGRAM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROGRAM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROGRAM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PROGRAM):
    case SUCCESS(ACTION_TYPES.UPDATE_PROGRAM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PROGRAM):
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

const apiUrl = SERVICENET_API_URL + '/programs';

// Actions

export const getEntities: ICrudGetAllAction<IProgram> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PROGRAM_LIST,
  payload: axios.get<IProgram>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IProgram> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PROGRAM,
    payload: axios.get<IProgram>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IProgram> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PROGRAM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProgram> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PROGRAM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProgram> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PROGRAM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
