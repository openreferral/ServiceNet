import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DataImportReport from './data-import-report';
import DataImportReportDetail from './data-import-report-detail';
import DataImportReportUpdate from './data-import-report-update';
import DataImportReportDeleteDialog from './data-import-report-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DataImportReportUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DataImportReportUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DataImportReportDetail} />
      <ErrorBoundaryRoute path={match.url} component={DataImportReport} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DataImportReportDeleteDialog} />
  </>
);

export default Routes;
