import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ExclusionsConfig from './exclusions-config';
import ExclusionsConfigDetail from './exclusions-config-detail';
import ExclusionsConfigUpdate from './exclusions-config-update';
import ExclusionsConfigDeleteDialog from './exclusions-config-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ExclusionsConfigUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ExclusionsConfigUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ExclusionsConfigDetail} />
      <ErrorBoundaryRoute path={match.url} component={ExclusionsConfig} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ExclusionsConfigDeleteDialog} />
  </>
);

export default Routes;
