import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RequiredDocument from './required-document';
import RequiredDocumentDetail from './required-document-detail';
import RequiredDocumentUpdate from './required-document-update';
import RequiredDocumentDeleteDialog from './required-document-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RequiredDocumentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RequiredDocumentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RequiredDocumentDetail} />
      <ErrorBoundaryRoute path={match.url} component={RequiredDocument} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RequiredDocumentDeleteDialog} />
  </>
);

export default Routes;
