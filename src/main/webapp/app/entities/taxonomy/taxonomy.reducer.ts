import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITaxonomy, defaultValue } from 'app/shared/model/taxonomy.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_TAXONOMY_LIST: 'taxonomy/FETCH_TAXONOMY_LIST',
  FETCH_TAXONOMY: 'taxonomy/FETCH_TAXONOMY',
  CREATE_TAXONOMY: 'taxonomy/CREATE_TAXONOMY',
  UPDATE_TAXONOMY: 'taxonomy/UPDATE_TAXONOMY',
  DELETE_TAXONOMY: 'taxonomy/DELETE_TAXONOMY',
  RESET: 'taxonomy/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITaxonomy>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type TaxonomyState = Readonly<typeof initialState>;

// Reducer

export default (state: TaxonomyState = initialState, action): TaxonomyState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TAXONOMY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TAXONOMY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TAXONOMY):
    case REQUEST(ACTION_TYPES.UPDATE_TAXONOMY):
    case REQUEST(ACTION_TYPES.DELETE_TAXONOMY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TAXONOMY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TAXONOMY):
    case FAILURE(ACTION_TYPES.CREATE_TAXONOMY):
    case FAILURE(ACTION_TYPES.UPDATE_TAXONOMY):
    case FAILURE(ACTION_TYPES.DELETE_TAXONOMY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TAXONOMY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_TAXONOMY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TAXONOMY):
    case SUCCESS(ACTION_TYPES.UPDATE_TAXONOMY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TAXONOMY):
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

const apiUrl = SERVICENET_API_URL + '/taxonomies';

// Actions

export const getEntities: ICrudGetAllAction<ITaxonomy> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TAXONOMY_LIST,
    payload: axios.get<ITaxonomy>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ITaxonomy> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TAXONOMY,
    payload: axios.get<ITaxonomy>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITaxonomy> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TAXONOMY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITaxonomy> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TAXONOMY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITaxonomy> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TAXONOMY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
