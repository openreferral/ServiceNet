import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Option from './option';
import OptionDetail from './option-detail';
import OptionUpdate from './option-update';
import OptionDeleteDialog from './option-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OptionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OptionDetail} />
      <ErrorBoundaryRoute path={match.url} component={Option} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={OptionDeleteDialog} />
  </>
);

export default Routes;
