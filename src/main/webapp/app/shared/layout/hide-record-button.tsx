import './hide-record-button.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Tooltip } from 'reactstrap';

interface IHideRecordButtonState {
  tooltipOpen: boolean;
}

interface IHideRecordButtonProps {
  handleHide: Function;
  id: string;
}

class HideRecordButton extends React.Component<IHideRecordButtonProps, IHideRecordButtonState> {
  state: IHideRecordButtonState = {
    tooltipOpen: false
  };

  toggle = () => {
    this.setState({
      tooltipOpen: !this.state.tooltipOpen
    });
  };

  handleClick = event => {
    this.props.handleHide(event);
  };

  render() {
    return (
      <div>
        <div onClick={this.handleClick} className="close-icon" id={this.props.id}>
          <FontAwesomeIcon icon="times" />
        </div>
        <Tooltip
          placement="bottom"
          innerClassName="tooltip-clip-inner"
          className="tooltip-clip"
          isOpen={this.state.tooltipOpen}
          target={this.props.id}
          toggle={this.toggle}
          autohide
        >
          <Translate contentKey="serviceNetApp.activity.hideRecord" />
        </Tooltip>
      </div>
    );
  }
}

export default HideRecordButton;
