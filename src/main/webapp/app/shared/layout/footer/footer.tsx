import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';

const Footer = props => (
  <footer className="footer d-flex align-items-center justify-content-between pl-4 pr-4">
    <Translate contentKey="footerLeft" interpolate={{ year: new Date().getFullYear() }} />
    <div className="link-content">
      <Translate contentKey="footerRight" />
    </div>
  </footer>
);

export default Footer;
