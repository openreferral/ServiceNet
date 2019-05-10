import ReactGA from 'react-ga';

import { REQUEST } from './../shared/reducers/action-type.util';
import { ACTION_TYPES as AUTH_ACTIONS } from './../shared/reducers/authentication';
import { ACTION_TYPES as SINGLE_RECORD_ACTIONS } from './../modules/conflicts/single/single-record-view.reducer';
import { ACTION_TYPES as MULTIPLE_RECORDS_ACTIONS } from './../modules/conflicts/multiple/multiple-record-view.reducer';

const ACTIONS_CATEGORY = 'UserActions';

const TRACKED_ACTIONS = {
  [REQUEST(AUTH_ACTIONS.LOGIN)]: 'Log In',
  [REQUEST(AUTH_ACTIONS.LOGOUT)]: 'Log Out',
  [REQUEST(SINGLE_RECORD_ACTIONS.FETCH_ACTIVITY_DETAILS)]: 'Clicking On A Record',
  [REQUEST(MULTIPLE_RECORDS_ACTIONS.FETCH_BASE_ORGANIZATION)]: 'Clicking On Side By Side View'
};

export default () => next => action => {
  const trackedAction = TRACKED_ACTIONS[action.type];

  if (trackedAction) {
    ReactGA.event({ category: ACTIONS_CATEGORY, action: trackedAction });
  }

  return next(action);
};
