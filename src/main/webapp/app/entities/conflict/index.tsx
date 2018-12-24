import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Conflict from './conflict';
import ConflictDetail from './conflict-detail';
import ConflictUpdate from './conflict-update';
import ConflictDeleteDialog from './conflict-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ConflictUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ConflictUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ConflictDetail} />
      <ErrorBoundaryRoute path={match.url} component={Conflict} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ConflictDeleteDialog} />
  </>
);

export default Routes;
