import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Language from './language';
import LanguageDetail from './language-detail';
import LanguageUpdate from './language-update';
import LanguageDeleteDialog from './language-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LanguageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LanguageUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LanguageDetail} />
      <ErrorBoundaryRoute path={match.url} component={Language} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={LanguageDeleteDialog} />
  </>
);

export default Routes;
