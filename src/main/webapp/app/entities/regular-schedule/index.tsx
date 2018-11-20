import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RegularSchedule from './regular-schedule';
import RegularScheduleDetail from './regular-schedule-detail';
import RegularScheduleUpdate from './regular-schedule-update';
import RegularScheduleDeleteDialog from './regular-schedule-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RegularScheduleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RegularScheduleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RegularScheduleDetail} />
      <ErrorBoundaryRoute path={match.url} component={RegularSchedule} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RegularScheduleDeleteDialog} />
  </>
);

export default Routes;
