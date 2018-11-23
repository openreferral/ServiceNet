import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PaymentAccepted from './payment-accepted';
import PaymentAcceptedDetail from './payment-accepted-detail';
import PaymentAcceptedUpdate from './payment-accepted-update';
import PaymentAcceptedDeleteDialog from './payment-accepted-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PaymentAcceptedUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PaymentAcceptedUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PaymentAcceptedDetail} />
      <ErrorBoundaryRoute path={match.url} component={PaymentAccepted} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PaymentAcceptedDeleteDialog} />
  </>
);

export default Routes;
