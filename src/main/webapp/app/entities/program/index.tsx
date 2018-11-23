import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Program from './program';
import ProgramDetail from './program-detail';
import ProgramUpdate from './program-update';
import ProgramDeleteDialog from './program-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProgramUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProgramUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProgramDetail} />
      <ErrorBoundaryRoute path={match.url} component={Program} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ProgramDeleteDialog} />
  </>
);

export default Routes;
