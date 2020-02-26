import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITaxonomyGroup, defaultValue } from 'app/shared/model/taxonomy-group.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_TAXONOMYGROUP_LIST: 'taxonomyGroup/FETCH_TAXONOMYGROUP_LIST',
  FETCH_TAXONOMYGROUP: 'taxonomyGroup/FETCH_TAXONOMYGROUP',
  CREATE_TAXONOMYGROUP: 'taxonomyGroup/CREATE_TAXONOMYGROUP',
  UPDATE_TAXONOMYGROUP: 'taxonomyGroup/UPDATE_TAXONOMYGROUP',
  DELETE_TAXONOMYGROUP: 'taxonomyGroup/DELETE_TAXONOMYGROUP',
  RESET: 'taxonomyGroup/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITaxonomyGroup>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type TaxonomyGroupState = Readonly<typeof initialState>;

// Reducer

export default (state: TaxonomyGroupState = initialState, action): TaxonomyGroupState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TAXONOMYGROUP_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TAXONOMYGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TAXONOMYGROUP):
    case REQUEST(ACTION_TYPES.UPDATE_TAXONOMYGROUP):
    case REQUEST(ACTION_TYPES.DELETE_TAXONOMYGROUP):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TAXONOMYGROUP_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TAXONOMYGROUP):
    case FAILURE(ACTION_TYPES.CREATE_TAXONOMYGROUP):
    case FAILURE(ACTION_TYPES.UPDATE_TAXONOMYGROUP):
    case FAILURE(ACTION_TYPES.DELETE_TAXONOMYGROUP):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TAXONOMYGROUP_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_TAXONOMYGROUP):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TAXONOMYGROUP):
    case SUCCESS(ACTION_TYPES.UPDATE_TAXONOMYGROUP):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TAXONOMYGROUP):
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

const apiUrl = SERVICENET_API_URL + '/taxonomy-groups';

// Actions

export const getEntities: ICrudGetAllAction<ITaxonomyGroup> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_TAXONOMYGROUP_LIST,
    payload: axios.get<ITaxonomyGroup>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ITaxonomyGroup> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TAXONOMYGROUP,
    payload: axios.get<ITaxonomyGroup>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITaxonomyGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TAXONOMYGROUP,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITaxonomyGroup> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TAXONOMYGROUP,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITaxonomyGroup> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TAXONOMYGROUP,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
