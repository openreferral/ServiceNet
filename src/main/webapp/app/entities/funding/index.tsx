import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Funding from './funding';
import FundingDetail from './funding-detail';
import FundingUpdate from './funding-update';
import FundingDeleteDialog from './funding-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FundingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FundingUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FundingDetail} />
      <ErrorBoundaryRoute path={match.url} component={Funding} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FundingDeleteDialog} />
  </>
);

export default Routes;
