import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AccessibilityForDisabilities from './accessibility-for-disabilities';
import AccessibilityForDisabilitiesDetail from './accessibility-for-disabilities-detail';
import AccessibilityForDisabilitiesUpdate from './accessibility-for-disabilities-update';
import AccessibilityForDisabilitiesDeleteDialog from './accessibility-for-disabilities-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AccessibilityForDisabilitiesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AccessibilityForDisabilitiesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AccessibilityForDisabilitiesDetail} />
      <ErrorBoundaryRoute path={match.url} component={AccessibilityForDisabilities} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AccessibilityForDisabilitiesDeleteDialog} />
  </>
);

export default Routes;
