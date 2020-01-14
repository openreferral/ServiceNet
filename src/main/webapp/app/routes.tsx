import React from 'react';
import { Switch } from 'react-router-dom';
import Loadable from 'react-loadable';

import Login from 'app/modules/login/login';
import Register from 'app/modules/account/register/register';
import Activate from 'app/modules/account/activate/activate';
import PasswordResetInit from 'app/modules/account/password-reset/init/password-reset-init';
import PasswordResetFinish from 'app/modules/account/password-reset/finish/password-reset-finish';
import Logout from 'app/modules/login/logout';
import Home from 'app/modules/home/home';
import Entities from 'app/entities';
import PrivateRoute from 'app/shared/auth/private-route';
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import { AUTHORITIES } from 'app/config/constants';
import UploadPage from './modules/upload/upload-page';
import SingleRecordView from './modules/conflicts/single/single-record-view';
import MultipleRecordView from './modules/conflicts/multiple/multiple-record-view';
import DismissedMatches from './modules/conflicts/dismissed/dismissed-matches';
import DismissedMatchView from './modules/conflicts/dismissed/dismissed-match-view';
import HiddenMatches from './modules/conflicts/hidden/hidden-matches';
import { AboutUs } from 'app/modules/about-us/about-us';
import Shelters from 'app/modules/shelter/shelters';
import MyShelters from 'app/modules/shelter/my-shelters';
import ShelterUpdate from 'app/entities/shelter/shelter-update';
import ShelterDetails from 'app/modules/shelter/shelter-details';
import AllRecordsView from 'app/modules/conflicts/all/all-records-view';

// tslint:disable:space-in-parens
const Account = Loadable({
  loader: () => import(/* webpackChunkName: "account" */ 'app/modules/account'),
  loading: () => <div>loading ...</div>
});

const Admin = Loadable({
  loader: () => import(/* webpackChunkName: "administration" */ 'app/modules/administration'),
  loading: () => <div>loading ...</div>
});
// tslint:enable

const Routes = ({ isAdmin, isSacramento }) => (
  <div className="view-routes">
    <ErrorBoundaryRoute path="/login" component={Login} />
    <Switch>
      <ErrorBoundaryRoute path="/logout" component={Logout} />
      <ErrorBoundaryRoute path="/register" component={Register} />
      <ErrorBoundaryRoute path="/activate/:key?" component={Activate} />
      <ErrorBoundaryRoute path="/reset/request" component={PasswordResetInit} />
      <ErrorBoundaryRoute path="/reset/finish/:key?" component={PasswordResetFinish} />
      <PrivateRoute path="/shelters" component={Shelters} hasAnyAuthorities={[AUTHORITIES.SACRAMENTO]} isAdmin={isAdmin} />
      <PrivateRoute path="/my-shelters" component={MyShelters} hasAnyAuthorities={[AUTHORITIES.SACRAMENTO]} isAdmin={isAdmin} />
      <PrivateRoute path="/shelter/:id/edit" component={ShelterUpdate} hasAnyAuthorities={[AUTHORITIES.SACRAMENTO]} isAdmin={isAdmin} />
      <PrivateRoute path="/shelter/:id" component={ShelterDetails} hasAnyAuthorities={[AUTHORITIES.SACRAMENTO]} isAdmin={isAdmin} />
      <PrivateRoute path="/admin" isAdmin={isAdmin} component={Admin} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute path="/account" isAdmin={isAdmin} component={Account} hasAnyAuthorities={[AUTHORITIES.ADMIN, AUTHORITIES.USER]} />
      <PrivateRoute path="/entity" isAdmin={isAdmin} component={Entities} hasAnyAuthorities={[AUTHORITIES.USER]} />
      <PrivateRoute path="/upload" isAdmin={isAdmin} component={UploadPage} hasAnyAuthorities={[AUTHORITIES.ADMIN]} />
      <PrivateRoute
        path="/single-record-view/:orgId?"
        isAdmin={isAdmin}
        component={SingleRecordView}
        hasAnyAuthorities={[AUTHORITIES.USER]}
      />
      <PrivateRoute
        path="/multi-record-view/:orgId?/:partnerId?"
        isAdmin={isAdmin}
        component={MultipleRecordView}
        hasAnyAuthorities={[AUTHORITIES.USER]}
      />
      <PrivateRoute path="/all-records-view/:orgId?" isAdmin={isAdmin} component={AllRecordsView} hasAnyAuthorities={[AUTHORITIES.USER]} />
      <PrivateRoute
        path="/dismissed-matches/:orgId"
        isAdmin={isAdmin}
        component={DismissedMatches}
        hasAnyAuthorities={[AUTHORITIES.USER]}
      />
      <PrivateRoute
        path="/dismissed-match-view/:orgId/partner/:partnerId"
        isAdmin={isAdmin}
        component={DismissedMatchView}
        hasAnyAuthorities={[AUTHORITIES.USER]}
      />
      <PrivateRoute path="/hidden-matches/" isAdmin={isAdmin} component={HiddenMatches} hasAnyAuthorities={[AUTHORITIES.USER]} />
      <ErrorBoundaryRoute path="/about-us" component={AboutUs} />
      {!isSacramento && <ErrorBoundaryRoute path="/" component={Home} />}
    </Switch>
  </div>
);

export default Routes;
