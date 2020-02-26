import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IServiceTaxonomiesDetailsFieldsValue, defaultValue } from 'app/shared/model/service-taxonomies-details-fields-value.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_SERVICETAXONOMIESDETAILSFIELDSVALUE_LIST: 'serviceTaxonomiesDetailsFieldsValue/FETCH_SERVICETAXONOMIESDETAILSFIELDSVALUE_LIST',
  FETCH_SERVICETAXONOMIESDETAILSFIELDSVALUE: 'serviceTaxonomiesDetailsFieldsValue/FETCH_SERVICETAXONOMIESDETAILSFIELDSVALUE',
  CREATE_SERVICETAXONOMIESDETAILSFIELDSVALUE: 'serviceTaxonomiesDetailsFieldsValue/CREATE_SERVICETAXONOMIESDETAILSFIELDSVALUE',
  UPDATE_SERVICETAXONOMIESDETAILSFIELDSVALUE: 'serviceTaxonomiesDetailsFieldsValue/UPDATE_SERVICETAXONOMIESDETAILSFIELDSVALUE',
  DELETE_SERVICETAXONOMIESDETAILSFIELDSVALUE: 'serviceTaxonomiesDetailsFieldsValue/DELETE_SERVICETAXONOMIESDETAILSFIELDSVALUE',
  RESET: 'serviceTaxonomiesDetailsFieldsValue/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IServiceTaxonomiesDetailsFieldsValue>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ServiceTaxonomiesDetailsFieldsValueState = Readonly<typeof initialState>;

// Reducer

export default (state: ServiceTaxonomiesDetailsFieldsValueState = initialState, action): ServiceTaxonomiesDetailsFieldsValueState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SERVICETAXONOMIESDETAILSFIELDSVALUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SERVICETAXONOMIESDETAILSFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SERVICETAXONOMIESDETAILSFIELDSVALUE):
    case REQUEST(ACTION_TYPES.UPDATE_SERVICETAXONOMIESDETAILSFIELDSVALUE):
    case REQUEST(ACTION_TYPES.DELETE_SERVICETAXONOMIESDETAILSFIELDSVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SERVICETAXONOMIESDETAILSFIELDSVALUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SERVICETAXONOMIESDETAILSFIELDSVALUE):
    case FAILURE(ACTION_TYPES.CREATE_SERVICETAXONOMIESDETAILSFIELDSVALUE):
    case FAILURE(ACTION_TYPES.UPDATE_SERVICETAXONOMIESDETAILSFIELDSVALUE):
    case FAILURE(ACTION_TYPES.DELETE_SERVICETAXONOMIESDETAILSFIELDSVALUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVICETAXONOMIESDETAILSFIELDSVALUE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVICETAXONOMIESDETAILSFIELDSVALUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SERVICETAXONOMIESDETAILSFIELDSVALUE):
    case SUCCESS(ACTION_TYPES.UPDATE_SERVICETAXONOMIESDETAILSFIELDSVALUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SERVICETAXONOMIESDETAILSFIELDSVALUE):
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

const apiUrl = SERVICENET_API_URL + '/service-taxonomies-details-fields-values';

// Actions

export const getEntities: ICrudGetAllAction<IServiceTaxonomiesDetailsFieldsValue> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SERVICETAXONOMIESDETAILSFIELDSVALUE_LIST,
  payload: axios.get<IServiceTaxonomiesDetailsFieldsValue>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IServiceTaxonomiesDetailsFieldsValue> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SERVICETAXONOMIESDETAILSFIELDSVALUE,
    payload: axios.get<IServiceTaxonomiesDetailsFieldsValue>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IServiceTaxonomiesDetailsFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SERVICETAXONOMIESDETAILSFIELDSVALUE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IServiceTaxonomiesDetailsFieldsValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SERVICETAXONOMIESDETAILSFIELDSVALUE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IServiceTaxonomiesDetailsFieldsValue> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SERVICETAXONOMIESDETAILSFIELDSVALUE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
