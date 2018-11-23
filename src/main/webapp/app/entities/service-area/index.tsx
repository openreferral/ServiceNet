import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ServiceArea from './service-area';
import ServiceAreaDetail from './service-area-detail';
import ServiceAreaUpdate from './service-area-update';
import ServiceAreaDeleteDialog from './service-area-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ServiceAreaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ServiceAreaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ServiceAreaDetail} />
      <ErrorBoundaryRoute path={match.url} component={ServiceArea} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ServiceAreaDeleteDialog} />
  </>
);

export default Routes;
