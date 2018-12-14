import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from '../header-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name={translate('global.menu.entities.main')} id="entity-menu">
    <DropdownItem tag={Link} to="/entity/system-account">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.systemAccount" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/document-upload">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.documentUpload" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/organization">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.organization" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/service">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.service" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/program">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.program" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/service-at-location">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.serviceAtLocation" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/location">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.location" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/physical-address">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.physicalAddress" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/postal-address">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.postalAddress" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/phone">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.phone" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/contact">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.contact" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/regular-schedule">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.regularSchedule" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/holiday-schedule">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.holidaySchedule" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/funding">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.funding" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/eligibility">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.eligibility" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/service-area">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.serviceArea" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/required-document">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.requiredDocument" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/payment-accepted">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.paymentAccepted" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/language">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.language" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/accessibility-for-disabilities">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.accessibilityForDisabilities" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/service-taxonomy">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.serviceTaxonomy" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/taxonomy">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.taxonomy" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/organization-match">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.organizationMatch" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/metadata">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.metadata" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/document-upload">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.documentUpload" />
    </DropdownItem>
    <DropdownItem tag={Link} to="/entity/activity">
      <FontAwesomeIcon icon="asterisk" />
      &nbsp;
      <Translate contentKey="global.menu.entities.activity" />
    </DropdownItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
