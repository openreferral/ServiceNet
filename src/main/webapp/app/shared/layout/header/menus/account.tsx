import React from 'react';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { NavLink as Link } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from '../header-components';

const accountMenuItemsAuthenticated = isAdmin => (
  <>
    <DropdownItem tag={Link} to="/account/settings">
      <FontAwesomeIcon icon="wrench" /> <Translate contentKey="global.menu.account.settings">Settings</Translate>
    </DropdownItem>
    <DropdownItem tag={Link} to="/account/password">
      <FontAwesomeIcon icon="clock" /> <Translate contentKey="global.menu.account.password">Password</Translate>
    </DropdownItem>
    <DropdownItem tag={Link} to="/hidden-matches">
      <FontAwesomeIcon icon="eye" /> <Translate contentKey="global.menu.admin.hiddenRecords">View hidden matches</Translate>
    </DropdownItem>
    {isAdmin && (
      <DropdownItem tag={Link} to="/account/sessions">
        <FontAwesomeIcon icon="cloud" /> <Translate contentKey="global.menu.account.sessions">Sessions</Translate>
      </DropdownItem>
    )}
    <DropdownItem tag={Link} to="/logout">
      <FontAwesomeIcon icon="sign-out-alt" /> <Translate contentKey="global.menu.account.logout">Sign out</Translate>
    </DropdownItem>
  </>
);

const accountMenuItems = (
  <>
    <DropdownItem id="login-item" tag={Link} to="/login">
      <FontAwesomeIcon icon="sign-in-alt" /> <Translate contentKey="global.menu.account.login">Sign in</Translate>
    </DropdownItem>
    <DropdownItem tag={Link} to="/register">
      <FontAwesomeIcon icon="sign-in-alt" /> <Translate contentKey="global.menu.account.register">Register</Translate>
    </DropdownItem>
  </>
);

export const AccountMenu = ({ isAuthenticated = false, userLogin, isAdmin = false }) => (
  <NavDropdown icon="user" name={userLogin ? userLogin : translate('global.menu.account.main')} id="account-menu">
    {isAuthenticated ? accountMenuItemsAuthenticated(isAdmin) : accountMenuItems}
  </NavDropdown>
);

export default AccountMenu;
