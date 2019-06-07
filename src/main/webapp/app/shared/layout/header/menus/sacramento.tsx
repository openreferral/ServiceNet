import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavItem, NavLink } from 'reactstrap';

export const SacramentoMenu = props => (
  <NavItem>
    <NavLink exact tag={Link} to="/shelters" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span className="navbar-label">
        <Translate contentKey="global.menu.shelters" />
      </span>
    </NavLink>
  </NavItem>
);
