import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SystemAccount from './system-account';
import DocumentUpload from './document-upload';
import Organization from './organization';
import Service from './service';
import Program from './program';
import ServiceAtLocation from './service-at-location';
import Location from './location';
import PhysicalAddress from './physical-address';
import PostalAddress from './postal-address';
import Phone from './phone';
import Contact from './contact';
import RegularSchedule from './regular-schedule';
import HolidaySchedule from './holiday-schedule';
import Funding from './funding';
import Eligibility from './eligibility';
import ServiceArea from './service-area';
import RequiredDocument from './required-document';
import PaymentAccepted from './payment-accepted';
import Language from './language';
import AccessibilityForDisabilities from './accessibility-for-disabilities';
import ServiceTaxonomy from './service-taxonomy';
import Taxonomy from './taxonomy';
import OrganizationMatch from './organization-match';
import Metadata from './metadata';
import OpeningHours from './opening-hours';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/system-account`} component={SystemAccount} />
      <ErrorBoundaryRoute path={`${match.url}/document-upload`} component={DocumentUpload} />
      <ErrorBoundaryRoute path={`${match.url}/organization`} component={Organization} />
      <ErrorBoundaryRoute path={`${match.url}/service`} component={Service} />
      <ErrorBoundaryRoute path={`${match.url}/program`} component={Program} />
      <ErrorBoundaryRoute path={`${match.url}/service-at-location`} component={ServiceAtLocation} />
      <ErrorBoundaryRoute path={`${match.url}/location`} component={Location} />
      <ErrorBoundaryRoute path={`${match.url}/physical-address`} component={PhysicalAddress} />
      <ErrorBoundaryRoute path={`${match.url}/postal-address`} component={PostalAddress} />
      <ErrorBoundaryRoute path={`${match.url}/phone`} component={Phone} />
      <ErrorBoundaryRoute path={`${match.url}/contact`} component={Contact} />
      <ErrorBoundaryRoute path={`${match.url}/regular-schedule`} component={RegularSchedule} />
      <ErrorBoundaryRoute path={`${match.url}/holiday-schedule`} component={HolidaySchedule} />
      <ErrorBoundaryRoute path={`${match.url}/funding`} component={Funding} />
      <ErrorBoundaryRoute path={`${match.url}/eligibility`} component={Eligibility} />
      <ErrorBoundaryRoute path={`${match.url}/service-area`} component={ServiceArea} />
      <ErrorBoundaryRoute path={`${match.url}/required-document`} component={RequiredDocument} />
      <ErrorBoundaryRoute path={`${match.url}/payment-accepted`} component={PaymentAccepted} />
      <ErrorBoundaryRoute path={`${match.url}/language`} component={Language} />
      <ErrorBoundaryRoute path={`${match.url}/accessibility-for-disabilities`} component={AccessibilityForDisabilities} />
      <ErrorBoundaryRoute path={`${match.url}/service-taxonomy`} component={ServiceTaxonomy} />
      <ErrorBoundaryRoute path={`${match.url}/taxonomy`} component={Taxonomy} />
      <ErrorBoundaryRoute path={`${match.url}/organization-match`} component={OrganizationMatch} />
      <ErrorBoundaryRoute path={`${match.url}/metadata`} component={Metadata} />
      <ErrorBoundaryRoute path={`${match.url}/opening-hours`} component={OpeningHours} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
