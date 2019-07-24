import React from 'react';
import { Translate } from 'react-jhipster';

import { UncontrolledDropdown, DropdownToggle, DropdownMenu, NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import appConfig from 'app/config/constants';

export const NavDropdown = props => (
  <UncontrolledDropdown nav inNavbar id={props.id}>
    <DropdownToggle nav caret className="d-flex align-items-center">
      <FontAwesomeIcon icon={props.icon} />
      <span className="navbar-label">{props.name}</span>
    </DropdownToggle>
    <DropdownMenu right style={props.style}>
      {props.children}
    </DropdownMenu>
  </UncontrolledDropdown>
);

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/benetech-logo.png" alt="Logo" />
  </div>
);

export const Brand = props => (
  <div className="d-flex align-items-center">
    <NavbarBrand tag={Link} to={props.isSacramento ? '/shelters' : '/'} className="brand-logo d-flex align-items-center mr-1">
      <BrandIcon />
      <span className="navbar-version mt-1">{appConfig.VERSION}</span>
    </NavbarBrand>
    <NavLink exact tag={Link} to="/about-us" className="pl-0">
      <span className="navbar-label text-dark about-us-link">
        <Translate contentKey="global.menu.aboutUs" />
      </span>
    </NavLink>
  </div>
);

export const Home = props => (
  <NavItem>
    <NavLink exact tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span className="navbar-label">
        <Translate contentKey="global.menu.home" />
      </span>
    </NavLink>
  </NavItem>
);

export const Upload = props => (
  <NavItem>
    <NavLink tag={Link} to="/upload" className="d-flex align-items-center">
      <FontAwesomeIcon icon="file-upload" />
      <span className="navbar-label">
        <Translate contentKey="global.menu.upload" />
      </span>
    </NavLink>
  </NavItem>
);
