import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Service from './service';
import ServiceDetail from './service-detail';
import ServiceUpdate from './service-update';
import ServiceDeleteDialog from './service-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ServiceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ServiceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ServiceDetail} />
      <ErrorBoundaryRoute path={match.url} component={Service} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ServiceDeleteDialog} />
  </>
);

export default Routes;
