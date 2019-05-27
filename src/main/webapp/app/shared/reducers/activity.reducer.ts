import axios from 'axios';
import { parseHeaderForLinks, loadMoreDataWhenScrolled, ICrudGetAction } from 'react-jhipster';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IActivity, defaultValue } from 'app/shared/model/activity.model';
import _ from 'lodash';

export const ACTION_TYPES = {
  FETCH_ACTIVITY_LIST: 'activity/FETCH_ACTIVITY_LIST',
  FETCH_ACTIVITY: 'activity/FETCH_ACTIVITY',
  CREATE_ACTIVITY: 'activity/CREATE_ACTIVITY',
  UPDATE_ACTIVITY: 'activity/UPDATE_ACTIVITY',
  DELETE_ACTIVITY: 'activity/DELETE_ACTIVITY',
  RESET: 'activity/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IActivity>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ActivityState = Readonly<typeof initialState>;

// Reducer

export default (state: ActivityState = initialState, action): ActivityState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ACTIVITY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACTIVITY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ACTIVITY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACTIVITY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVITY_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIVITY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/activities';

// Actions

export const getEntities = (search, page, size, sort, filter) => {
  const requestUrl = `${apiUrl}${sort ? `?search=${search}&page=${page}&size=${size}&sort=${sort}` : ''}`;

  const filterDataToSend = _.clone(filter);
  if (!_.isEmpty(filterDataToSend) && !_.isEmpty(filterDataToSend.partnerFilterList)) {
    filterDataToSend.partnerFilterList = _.map(filterDataToSend.partnerFilterList, partner => partner.value);
  }

  return {
    type: ACTION_TYPES.FETCH_ACTIVITY_LIST,
    payload: axios.post<IActivity>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`, filterDataToSend)
  };
};

export const getEntity: ICrudGetAction<IActivity> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIVITY,
    payload: axios.get<IActivity>(requestUrl)
  };
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
