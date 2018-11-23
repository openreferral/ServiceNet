import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HolidaySchedule from './holiday-schedule';
import HolidayScheduleDetail from './holiday-schedule-detail';
import HolidayScheduleUpdate from './holiday-schedule-update';
import HolidayScheduleDeleteDialog from './holiday-schedule-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HolidayScheduleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HolidayScheduleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HolidayScheduleDetail} />
      <ErrorBoundaryRoute path={match.url} component={HolidaySchedule} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={HolidayScheduleDeleteDialog} />
  </>
);

export default Routes;
