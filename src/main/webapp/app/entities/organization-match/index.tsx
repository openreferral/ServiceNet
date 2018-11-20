import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import OrganizationMatch from './organization-match';
import OrganizationMatchDetail from './organization-match-detail';
import OrganizationMatchUpdate from './organization-match-update';
import OrganizationMatchDeleteDialog from './organization-match-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OrganizationMatchUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OrganizationMatchUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OrganizationMatchDetail} />
      <ErrorBoundaryRoute path={match.url} component={OrganizationMatch} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OrganizationMatchDeleteDialog} />
  </>
);

export default Routes;
