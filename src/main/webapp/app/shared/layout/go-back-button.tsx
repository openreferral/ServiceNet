import React from 'react';
import { RouteComponentProps, withRouter } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import './go-back-button.scss';

class GoBackButton extends React.Component<RouteComponentProps> {
  goBack = () => {
    this.props.history.goBack();
  };

  render() {
    return (
      <div onClick={this.goBack} className="go-back-button">
        <FontAwesomeIcon icon="angle-left" />
        &nbsp;
        <Translate contentKey="global.goBack">Back</Translate>
      </div>
    );
  }
}

export default withRouter(GoBackButton);
