import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Shelter from './shelter';
import ShelterDetail from './shelter-detail';
import ShelterUpdate from './shelter-update';
import ShelterDeleteDialog from './shelter-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShelterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShelterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShelterDetail} />
      <ErrorBoundaryRoute path={match.url} component={Shelter} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ShelterDeleteDialog} />
  </>
);

export default Routes;
