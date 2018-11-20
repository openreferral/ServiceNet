import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DocumentUpload from './document-upload';
import DocumentUploadDetail from './document-upload-detail';
import DocumentUploadUpdate from './document-upload-update';
import DocumentUploadDeleteDialog from './document-upload-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DocumentUploadUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DocumentUploadUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DocumentUploadDetail} />
      <ErrorBoundaryRoute path={match.url} component={DocumentUpload} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DocumentUploadDeleteDialog} />
  </>
);

export default Routes;
