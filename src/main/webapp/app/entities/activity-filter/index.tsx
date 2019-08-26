import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ActivityFilter from './activity-filter';
import ActivityFilterDetail from './activity-filter-detail';
import ActivityFilterUpdate from './activity-filter-update';
import ActivityFilterDeleteDialog from './activity-filter-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ActivityFilterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ActivityFilterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ActivityFilterDetail} />
      <ErrorBoundaryRoute path={match.url} component={ActivityFilter} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ActivityFilterDeleteDialog} />
  </>
);

export default Routes;
