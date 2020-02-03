import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FieldsDisplaySettings from './fields-display-settings';
import FieldsDisplaySettingsDetail from './fields-display-settings-detail';
import FieldsDisplaySettingsUpdate from './fields-display-settings-update';
import FieldsDisplaySettingsDeleteDialog from './fields-display-settings-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FieldsDisplaySettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FieldsDisplaySettingsUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FieldsDisplaySettingsDetail} />
      <ErrorBoundaryRoute path={match.url} component={FieldsDisplaySettings} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FieldsDisplaySettingsDeleteDialog} />
  </>
);

export default Routes;
