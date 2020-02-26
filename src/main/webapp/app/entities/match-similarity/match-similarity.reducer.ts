import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMatchSimilarity, defaultValue } from 'app/shared/model/match-similarity.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_MATCHSIMILARITY_LIST: 'matchSimilarity/FETCH_MATCHSIMILARITY_LIST',
  FETCH_MATCHSIMILARITY: 'matchSimilarity/FETCH_MATCHSIMILARITY',
  CREATE_MATCHSIMILARITY: 'matchSimilarity/CREATE_MATCHSIMILARITY',
  UPDATE_MATCHSIMILARITY: 'matchSimilarity/UPDATE_MATCHSIMILARITY',
  DELETE_MATCHSIMILARITY: 'matchSimilarity/DELETE_MATCHSIMILARITY',
  RESET: 'matchSimilarity/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMatchSimilarity>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type MatchSimilarityState = Readonly<typeof initialState>;

// Reducer

export default (state: MatchSimilarityState = initialState, action): MatchSimilarityState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MATCHSIMILARITY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MATCHSIMILARITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MATCHSIMILARITY):
    case REQUEST(ACTION_TYPES.UPDATE_MATCHSIMILARITY):
    case REQUEST(ACTION_TYPES.DELETE_MATCHSIMILARITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_MATCHSIMILARITY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MATCHSIMILARITY):
    case FAILURE(ACTION_TYPES.CREATE_MATCHSIMILARITY):
    case FAILURE(ACTION_TYPES.UPDATE_MATCHSIMILARITY):
    case FAILURE(ACTION_TYPES.DELETE_MATCHSIMILARITY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_MATCHSIMILARITY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_MATCHSIMILARITY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MATCHSIMILARITY):
    case SUCCESS(ACTION_TYPES.UPDATE_MATCHSIMILARITY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MATCHSIMILARITY):
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

const apiUrl = SERVICENET_API_URL + '/match-similarities';

// Actions

export const getEntities: ICrudGetAllAction<IMatchSimilarity> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MATCHSIMILARITY_LIST,
    payload: axios.get<IMatchSimilarity>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IMatchSimilarity> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MATCHSIMILARITY,
    payload: axios.get<IMatchSimilarity>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMatchSimilarity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MATCHSIMILARITY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMatchSimilarity> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MATCHSIMILARITY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMatchSimilarity> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MATCHSIMILARITY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
