import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Metadata from './metadata';
import MetadataDetail from './metadata-detail';
import MetadataUpdate from './metadata-update';
import MetadataDeleteDialog from './metadata-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MetadataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MetadataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MetadataDetail} />
      <ErrorBoundaryRoute path={match.url} component={Metadata} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={MetadataDeleteDialog} />
  </>
);

export default Routes;
