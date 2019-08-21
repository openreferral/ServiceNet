import axios from 'axios';
import { parseHeaderForLinks, loadMoreDataWhenScrolled } from 'react-jhipster';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IActivity, defaultValue } from 'app/shared/model/activity.model';
import _ from 'lodash';

export const ACTION_TYPES = {
  FETCH_ACTIVITY_LIST: 'activity/FETCH_ACTIVITY_LIST',
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
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ACTIVITY_LIST):
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

  const filterDataToSend = {
    ...filter,
    partnerFilterList: _.map(filter.partnerFilterList, partner => partner.value)
  };

  return {
    type: ACTION_TYPES.FETCH_ACTIVITY_LIST,
    payload: axios.post<IActivity>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`, filterDataToSend)
  };
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
