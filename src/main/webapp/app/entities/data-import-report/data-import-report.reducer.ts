import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDataImportReport, defaultValue } from 'app/shared/model/data-import-report.model';
import { SERVICENET_API_URL } from 'app/shared/util/service-url.constants';

export const ACTION_TYPES = {
  FETCH_DATAIMPORTREPORT_LIST: 'dataImportReport/FETCH_DATAIMPORTREPORT_LIST',
  FETCH_DATAIMPORTREPORT: 'dataImportReport/FETCH_DATAIMPORTREPORT',
  CREATE_DATAIMPORTREPORT: 'dataImportReport/CREATE_DATAIMPORTREPORT',
  UPDATE_DATAIMPORTREPORT: 'dataImportReport/UPDATE_DATAIMPORTREPORT',
  DELETE_DATAIMPORTREPORT: 'dataImportReport/DELETE_DATAIMPORTREPORT',
  RESET: 'dataImportReport/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDataImportReport>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DataImportReportState = Readonly<typeof initialState>;

// Reducer

export default (state: DataImportReportState = initialState, action): DataImportReportState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DATAIMPORTREPORT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DATAIMPORTREPORT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DATAIMPORTREPORT):
    case REQUEST(ACTION_TYPES.UPDATE_DATAIMPORTREPORT):
    case REQUEST(ACTION_TYPES.DELETE_DATAIMPORTREPORT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DATAIMPORTREPORT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DATAIMPORTREPORT):
    case FAILURE(ACTION_TYPES.CREATE_DATAIMPORTREPORT):
    case FAILURE(ACTION_TYPES.UPDATE_DATAIMPORTREPORT):
    case FAILURE(ACTION_TYPES.DELETE_DATAIMPORTREPORT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DATAIMPORTREPORT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: action.payload.headers['x-total-count']
      };
    case SUCCESS(ACTION_TYPES.FETCH_DATAIMPORTREPORT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DATAIMPORTREPORT):
    case SUCCESS(ACTION_TYPES.UPDATE_DATAIMPORTREPORT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DATAIMPORTREPORT):
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

const apiUrl = SERVICENET_API_URL + '/data-import-reports';

// Actions

export const getEntities: ICrudGetAllAction<IDataImportReport> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DATAIMPORTREPORT_LIST,
    payload: axios.get<IDataImportReport>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDataImportReport> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DATAIMPORTREPORT,
    payload: axios.get<IDataImportReport>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDataImportReport> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DATAIMPORTREPORT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDataImportReport> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DATAIMPORTREPORT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDataImportReport> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DATAIMPORTREPORT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
