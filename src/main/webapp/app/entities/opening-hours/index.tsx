import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OpeningHours from './opening-hours';
import OpeningHoursDetail from './opening-hours-detail';
import OpeningHoursUpdate from './opening-hours-update';
import OpeningHoursDeleteDialog from './opening-hours-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OpeningHoursUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OpeningHoursUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OpeningHoursDetail} />
      <ErrorBoundaryRoute path={match.url} component={OpeningHours} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OpeningHoursDeleteDialog} />
  </>
);

export default Routes;
