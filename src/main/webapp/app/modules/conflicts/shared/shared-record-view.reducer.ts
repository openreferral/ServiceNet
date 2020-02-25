import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

export const ACTION_TYPES = {
  FETCH_BASE_ORGANIZATION: 'recordView/FETCH_BASE_ORGANIZATION',
  FETCH_PARTNER_ORGANIZATION: 'recordView/FETCH_PARTNER_ORGANIZATION',
  FETCH_PARTNER_ORGANIZATIONS: 'recordView/FETCH_PARTNER_ORGANIZATIONS',
  FETCH_MATCHES: 'recordView/FETCH_MATCHES',
  CREATE_SERVICE_MATCHES: 'recordView/CREATE_SERVICE_MATCHES',
  DELETE_SERVICE_MATCHES: 'recordView/DELETE_SERVICE_MATCHES',
  OPEN_SERVICE: 'OPEN_SERVICE',
  CREATE_LOCATION_MATCHES: 'recordView/CREATE_LOCATION_MATCHES',
  DELETE_LOCATION_MATCHES: 'recordView/DELETE_LOCATION_MATCHES',
  OPEN_LOCATION: 'OPEN_LOCATION'
};

const initialState = {
  errorMessage: null,
  baseRecord: null,
  partnerRecord: null,
  matches: [],
  dismissedMatches: [],
  hiddenMatches: [],
  partnerRecords: [],
  openService: null,
  openLocation: null
};

export type SharedRecordViewState = Readonly<typeof initialState>;

// Reducer
export default (state: SharedRecordViewState = initialState, action): SharedRecordViewState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_BASE_ORGANIZATION):
    case REQUEST(ACTION_TYPES.FETCH_PARTNER_ORGANIZATION):
    case REQUEST(ACTION_TYPES.FETCH_PARTNER_ORGANIZATIONS):
    case REQUEST(ACTION_TYPES.FETCH_MATCHES):
    case REQUEST(ACTION_TYPES.CREATE_SERVICE_MATCHES):
    case REQUEST(ACTION_TYPES.DELETE_SERVICE_MATCHES):
    case REQUEST(ACTION_TYPES.CREATE_LOCATION_MATCHES):
    case REQUEST(ACTION_TYPES.DELETE_LOCATION_MATCHES):
      return {
        ...state
      };
    case FAILURE(ACTION_TYPES.FETCH_BASE_ORGANIZATION):
    case FAILURE(ACTION_TYPES.FETCH_PARTNER_ORGANIZATION):
    case FAILURE(ACTION_TYPES.FETCH_PARTNER_ORGANIZATIONS):
    case FAILURE(ACTION_TYPES.FETCH_MATCHES):
    case FAILURE(ACTION_TYPES.CREATE_SERVICE_MATCHES):
    case FAILURE(ACTION_TYPES.DELETE_SERVICE_MATCHES):
    case FAILURE(ACTION_TYPES.CREATE_LOCATION_MATCHES):
    case FAILURE(ACTION_TYPES.DELETE_LOCATION_MATCHES):
      return {
        ...state,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_BASE_ORGANIZATION):
      return {
        ...state,
        baseRecord: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARTNER_ORGANIZATION):
      return {
        ...state,
        partnerRecord: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PARTNER_ORGANIZATIONS):
      return {
        ...state,
        partnerRecords: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MATCHES):
      const matches = [];
      const dismissedMatches = [];
      const hiddenMatches = [];

      action.payload.data.forEach(item => {
        if (item.hidden) {
          hiddenMatches.push(item);
        } else if (item.dismissed) {
          dismissedMatches.push(item);
        } else {
          matches.push(item);
        }
      });

      return {
        ...state,
        matches,
        dismissedMatches,
        hiddenMatches
      };
    case ACTION_TYPES.OPEN_SERVICE:
      return {
        ...state,
        openService: action.payload
      };
    case ACTION_TYPES.OPEN_LOCATION:
      return {
        ...state,
        openLocation: action.payload
      };
    default:
      return state;
  }
};

// Actions
const url = 'api/';
const activityUrl = url + 'activities/';
const partnerActivityUrl = url + 'partner-activities/';
const matchesUrl = url + 'organization-matches/organization/';
const hiddenMatchesUrl = url + 'organization-matches/hidden';
const matchService = url + 'service-matches';
const matchLocation = url + 'location-matches';

export const getBaseRecord = orgId => ({
  type: ACTION_TYPES.FETCH_BASE_ORGANIZATION,
  payload: axios.get(`${activityUrl + orgId}`)
});

export const getPartnerRecord = orgId => ({
  type: ACTION_TYPES.FETCH_PARTNER_ORGANIZATION,
  payload: axios.get(`${activityUrl + orgId}`)
});

export const getPartnerRecords = orgId => ({
  type: ACTION_TYPES.FETCH_PARTNER_ORGANIZATIONS,
  payload: axios.get(`${partnerActivityUrl + orgId}`)
});

export const getMatches = orgId => ({
  type: ACTION_TYPES.FETCH_MATCHES,
  payload: axios.get(`${matchesUrl + orgId}`)
});

export const getHiddenMatches = () => ({
  type: ACTION_TYPES.FETCH_MATCHES,
  payload: axios.get(hiddenMatchesUrl)
});

export const getNotHiddenMatchesByOrg = orgId => ({
  type: ACTION_TYPES.FETCH_MATCHES,
  payload: axios.get(matchesUrl + orgId + '/notHidden')
});

export const createServiceMatch = (serviceId, matchingServiceId, orgId) => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SERVICE_MATCHES,
    payload: axios.post(matchService, { service: serviceId, matchingService: matchingServiceId })
  });
  dispatch(getMatches(orgId));
  return result;
};

export const deleteServiceMatch = (serviceId, matchingServiceId, orgId) => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SERVICE_MATCHES,
    payload: axios.delete(matchService, {
      data: {
        service: serviceId,
        matchingService: matchingServiceId
      }
    })
  });
  dispatch(getMatches(orgId));
  return result;
};

export const setOpenService = serviceId => ({
  type: ACTION_TYPES.OPEN_SERVICE,
  payload: serviceId
});

export const createLocationMatch = (locationId, matchingLocationId, orgId) => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_LOCATION_MATCHES,
    payload: axios.post(matchLocation, { location: locationId, matchingLocation: matchingLocationId })
  });
  dispatch(getMatches(orgId));
  return result;
};

export const deleteLocationMatch = (locationId, matchingLocationId, orgId) => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_LOCATION_MATCHES,
    payload: axios.delete(matchLocation, {
      data: {
        location: locationId,
        matchingLocation: matchingLocationId
      }
    })
  });
  dispatch(getMatches(orgId));
  return result;
};

export const setOpenLocation = locationId => ({
  type: ACTION_TYPES.OPEN_LOCATION,
  payload: locationId
});
