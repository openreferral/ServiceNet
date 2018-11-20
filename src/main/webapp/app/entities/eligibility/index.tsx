import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Eligibility from './eligibility';
import EligibilityDetail from './eligibility-detail';
import EligibilityUpdate from './eligibility-update';
import EligibilityDeleteDialog from './eligibility-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EligibilityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EligibilityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EligibilityDetail} />
      <ErrorBoundaryRoute path={match.url} component={Eligibility} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={EligibilityDeleteDialog} />
  </>
);

export default Routes;
