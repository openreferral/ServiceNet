import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ConfidentialRecord from './confidential-record';
import ConfidentialRecordDetail from './confidential-record-detail';
import ConfidentialRecordUpdate from './confidential-record-update';
import ConfidentialRecordDeleteDialog from './confidential-record-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ConfidentialRecordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ConfidentialRecordUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ConfidentialRecordDetail} />
      <ErrorBoundaryRoute path={match.url} component={ConfidentialRecord} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ConfidentialRecordDeleteDialog} />
  </>
);

export default Routes;
