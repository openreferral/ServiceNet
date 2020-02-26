import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IServiceTaxonomy, defaultValue } from 'app/shared/model/service-taxonomy.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_SERVICETAXONOMY_LIST: 'serviceTaxonomy/FETCH_SERVICETAXONOMY_LIST',
  FETCH_SERVICETAXONOMY: 'serviceTaxonomy/FETCH_SERVICETAXONOMY',
  CREATE_SERVICETAXONOMY: 'serviceTaxonomy/CREATE_SERVICETAXONOMY',
  UPDATE_SERVICETAXONOMY: 'serviceTaxonomy/UPDATE_SERVICETAXONOMY',
  DELETE_SERVICETAXONOMY: 'serviceTaxonomy/DELETE_SERVICETAXONOMY',
  SET_BLOB: 'serviceTaxonomy/SET_BLOB',
  RESET: 'serviceTaxonomy/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IServiceTaxonomy>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ServiceTaxonomyState = Readonly<typeof initialState>;

// Reducer

export default (state: ServiceTaxonomyState = initialState, action): ServiceTaxonomyState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SERVICETAXONOMY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SERVICETAXONOMY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SERVICETAXONOMY):
    case REQUEST(ACTION_TYPES.UPDATE_SERVICETAXONOMY):
    case REQUEST(ACTION_TYPES.DELETE_SERVICETAXONOMY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SERVICETAXONOMY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SERVICETAXONOMY):
    case FAILURE(ACTION_TYPES.CREATE_SERVICETAXONOMY):
    case FAILURE(ACTION_TYPES.UPDATE_SERVICETAXONOMY):
    case FAILURE(ACTION_TYPES.DELETE_SERVICETAXONOMY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVICETAXONOMY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVICETAXONOMY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SERVICETAXONOMY):
    case SUCCESS(ACTION_TYPES.UPDATE_SERVICETAXONOMY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SERVICETAXONOMY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = SERVICENET_API_URL + '/service-taxonomies';

// Actions

export const getEntities: ICrudGetAllAction<IServiceTaxonomy> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_SERVICETAXONOMY_LIST,
    payload: axios.get<IServiceTaxonomy>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IServiceTaxonomy> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SERVICETAXONOMY,
    payload: axios.get<IServiceTaxonomy>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IServiceTaxonomy> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SERVICETAXONOMY,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IServiceTaxonomy> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SERVICETAXONOMY,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IServiceTaxonomy> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SERVICETAXONOMY,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
