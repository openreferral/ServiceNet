import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFieldsDisplaySettings, defaultValue } from 'app/shared/model/fields-display-settings.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_FIELDSDISPLAYSETTINGS_LIST: 'fieldsDisplaySettings/FETCH_FIELDSDISPLAYSETTINGS_LIST',
  FETCH_FIELDSDISPLAYSETTINGS: 'fieldsDisplaySettings/FETCH_FIELDSDISPLAYSETTINGS',
  CREATE_FIELDSDISPLAYSETTINGS: 'fieldsDisplaySettings/CREATE_FIELDSDISPLAYSETTINGS',
  UPDATE_FIELDSDISPLAYSETTINGS: 'fieldsDisplaySettings/UPDATE_FIELDSDISPLAYSETTINGS',
  DELETE_FIELDSDISPLAYSETTINGS: 'fieldsDisplaySettings/DELETE_FIELDSDISPLAYSETTINGS',
  UPDATE_SELECTED_SETTINGS: 'fieldsDisplaySettings/UPDATE_SELECTED_SETTINGS',
  RESET: 'fieldsDisplaySettings/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFieldsDisplaySettings>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
  selectedSettings: {
    id: null,
    locationFields: [],
    organizationFields: [],
    physicalAddressFields: [],
    postalAddressFields: [],
    serviceFields: [],
    serviceTaxonomiesDetailsFields: [],
    contactDetailsFields: []
  }
};

export type FieldsDisplaySettingsState = Readonly<typeof initialState>;

// Reducer

export default (state: FieldsDisplaySettingsState = initialState, action): FieldsDisplaySettingsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FIELDSDISPLAYSETTINGS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FIELDSDISPLAYSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FIELDSDISPLAYSETTINGS):
    case REQUEST(ACTION_TYPES.UPDATE_FIELDSDISPLAYSETTINGS):
    case REQUEST(ACTION_TYPES.DELETE_FIELDSDISPLAYSETTINGS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_FIELDSDISPLAYSETTINGS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FIELDSDISPLAYSETTINGS):
    case FAILURE(ACTION_TYPES.CREATE_FIELDSDISPLAYSETTINGS):
    case FAILURE(ACTION_TYPES.UPDATE_FIELDSDISPLAYSETTINGS):
    case FAILURE(ACTION_TYPES.DELETE_FIELDSDISPLAYSETTINGS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_FIELDSDISPLAYSETTINGS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_FIELDSDISPLAYSETTINGS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FIELDSDISPLAYSETTINGS):
    case SUCCESS(ACTION_TYPES.UPDATE_FIELDSDISPLAYSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FIELDSDISPLAYSETTINGS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.UPDATE_SELECTED_SETTINGS:
      return {
        ...state,
        selectedSettings: action.payload,
        loading: false
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = SERVICENET_API_URL + '/fields-display-settings';

// Actions

export const getEntities: ICrudGetAllAction<IFieldsDisplaySettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_FIELDSDISPLAYSETTINGS_LIST,
  payload: axios.get<IFieldsDisplaySettings>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getUserEntities: ICrudGetAllAction<IFieldsDisplaySettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_FIELDSDISPLAYSETTINGS_LIST,
  payload: axios.get<IFieldsDisplaySettings>(`${apiUrl}-by-user?cacheBuster=${new Date().getTime()}`)
});

export const getSystemAccountEntities: ICrudGetAllAction<IFieldsDisplaySettings> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_FIELDSDISPLAYSETTINGS_LIST,
  payload: axios.get<IFieldsDisplaySettings>(`${apiUrl}-by-system-account?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IFieldsDisplaySettings> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FIELDSDISPLAYSETTINGS,
    payload: axios.get<IFieldsDisplaySettings>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFieldsDisplaySettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FIELDSDISPLAYSETTINGS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFieldsDisplaySettings> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FIELDSDISPLAYSETTINGS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFieldsDisplaySettings> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FIELDSDISPLAYSETTINGS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const updateSelectedSettings = selectedSettings => ({
  type: ACTION_TYPES.UPDATE_SELECTED_SETTINGS,
  payload: selectedSettings
});
