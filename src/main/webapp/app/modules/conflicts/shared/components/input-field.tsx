import React from 'react';
import ReactGA from 'react-ga';
import { Col, Row, FormGroup, Label, Input, Tooltip } from 'reactstrap';
import classnames from 'classnames';
import { connect } from 'react-redux';
import { Translate, TextFormat, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import '../shared-record-view.scss';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

const DOMAIN_CLASS = 'org.benetech.servicenet.domain';

export interface IInputFieldProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  entityClass: string;
  fieldName: string;
  type: string;
  defaultValue: any;
  isBaseRecord: boolean;
  showClipboard: boolean;
}

export interface IInputFieldState {
  tooltipOpen: boolean;
  isConflicting: boolean;
  isExcluded: boolean;
  clipboardTooltipOpen: boolean;
  clipboardText: string;
}

interface IClipboard {
  writeText(newClipText: string): Promise<void>;
}

interface INavigator {
  readonly clipboard?: IClipboard;
}

export class InputField extends React.Component<IInputFieldProp, IInputFieldState> {
  state: IInputFieldState = {
    tooltipOpen: false,
    isConflicting: this.isConflictingField(this.props.fieldName),
    isExcluded: this.isExcludedField(this.props.fieldName),
    clipboardTooltipOpen: false,
    clipboardText: translate('multiRecordView.copyToClipboard')
  };

  getSuggestedValues(fieldName) {
    if (this.state.isConflicting) {
      return this.props.activity.conflicts.filter(e => e.fieldName === fieldName);
    }
    return null;
  }

  isConflictingField(fieldName) {
    const entityPath = DOMAIN_CLASS + '.' + this.props.entityClass;
    return this.isConflicting(fieldName, entityPath);
  }

  isExcludedField(fieldName) {
    const entityPath = DOMAIN_CLASS + '.' + this.props.entityClass;
    return this.isExcluded(fieldName, entityPath);
  }

  isConflicting(fieldName, entityPath) {
    return this.props.activity.conflicts.some(e => e.fieldName === fieldName && e.entityPath === entityPath) && this.props.isBaseRecord;
  }

  isExcluded(fieldName, entityPath) {
    return this.props.activity.exclusions.some(
      e =>
        e.fields
          .replace(' ', '')
          .split(',')
          .includes(fieldName) && e.entity === entityPath
    );
  }

  toggleTooltip = () => {
    this.setState({
      tooltipOpen: !this.state.tooltipOpen
    });
  };

  toggleClipboardTooltip = () => {
    this.setState({
      clipboardText: translate('multiRecordView.copyToClipboard'),
      clipboardTooltipOpen: !this.state.clipboardTooltipOpen
    });
  };

  getIdentifierName(entityClass, fieldName) {
    return entityClass.charAt(0).toLowerCase() + entityClass.slice(1) + fieldName.charAt(0).toUpperCase() + fieldName.slice(1);
  }

  copyToClipboard = text => () => {
    ReactGA.event({ category: 'UserActions', action: 'Copied Field' });

    const textArea = document.createElement('textarea');
    textArea.value = text;
    document.body.appendChild(textArea);
    textArea.select();

    const successful = document.execCommand('copy');
    if (successful) {
      this.setState({ clipboardText: translate('multiRecordView.copied') });
    }

    document.body.removeChild(textArea);
  };

  render() {
    const { entityClass, fieldName, type, defaultValue, showClipboard } = this.props;
    const identifier = this.getIdentifierName(entityClass, fieldName);
    const input = (
      <Input
        disabled
        className={classnames({ blueBorder: this.state.isConflicting, grayBorder: this.state.isExcluded })}
        type={type}
        name={identifier}
        id={identifier}
        value={fieldName === 'url' ? '' : defaultValue != null ? defaultValue : ''}
      />
    );
    const label = (
      <Label for={identifier}>
        <Translate contentKey={`singleRecordView.inputField.${identifier}`} />
      </Label>
    );
    let icon = null;
    if (this.state.isConflicting) {
      icon = (
        <div id={`${identifier}-icon`}>
          <FontAwesomeIcon className={`icon-conflicting icon-${type}`} size="lg" icon="question-circle" />
        </div>
      );
    } else if (this.state.isExcluded) {
      icon = (
        <div id={`${identifier}-icon`}>
          <FontAwesomeIcon className={`icon-excluded icon-${type}`} size="lg" icon="times-circle" />
        </div>
      );
    }

    const suggestedValues = this.getSuggestedValues(fieldName);
    let partnersNumber = 0;
    if (suggestedValues) {
      suggestedValues.forEach(v => {
        partnersNumber++;
      });
    }

    let tooltip = null;
    if (this.state.isConflicting) {
      tooltip = (
        <Tooltip
          placement="right"
          innerClassName="tooltip-inner"
          className="tooltip"
          autohide={false}
          isOpen={this.state.tooltipOpen}
          target={`${identifier}-icon`}
          toggle={this.toggleTooltip}
        >
          {partnersNumber}
          <Translate
            contentKey={`singleRecordView.inputField.${
              suggestedValues.length === 1 ? 'conflictingDataOnePartner' : 'conflictingDataMultiPartners'
            }`}
          />
          {suggestedValues.map((value, i) => (
            <div className="suggested" key={`suggested-${identifier}-${i}`}>
              <hr className="half-rule" />
              {value.offeredValue}
              <br />
              <div>
                <p className="secondary">
                  {value.partnerName}
                  <Translate contentKey="singleRecordView.inputField.imported" />
                  <TextFormat value={value.offeredValueDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
                </p>
              </div>
            </div>
          ))}
        </Tooltip>
      );
    } else if (this.state.isExcluded) {
      tooltip = (
        <Tooltip
          placement="right"
          innerClassName="tooltip-inner"
          className="tooltip"
          autohide={false}
          isOpen={this.state.tooltipOpen}
          target={`${identifier}-icon`}
          toggle={this.toggleTooltip}
        >
          <Translate contentKey="singleRecordView.details.excludedCopy" />
        </Tooltip>
      );
    }

    let clipboardButton = null;
    let clipboardTooltip = null;
    if (showClipboard && defaultValue) {
      clipboardButton = (
        <div className="copy-to-clipboard-icon" id={`${identifier}-clipboard`} onClick={this.copyToClipboard(defaultValue)}>
          <FontAwesomeIcon size="lg" icon="clipboard" />
        </div>
      );

      clipboardTooltip = (
        <Tooltip
          placement="bottom"
          innerClassName="tooltip-clip-inner"
          className="tooltip-clip"
          isOpen={this.state.clipboardTooltipOpen}
          target={`${identifier}-clipboard`}
          toggle={this.toggleClipboardTooltip}
          autohide
        >
          {this.state.clipboardText}
        </Tooltip>
      );
    }

    let urlButton = null;
    if (fieldName === 'url' && defaultValue) {
      urlButton = (
        <a className="url-link" style={{ color: 'rgba(0, 0, 0, 0.7)' }} href={this.props.activity.organization.url}>
          <FontAwesomeIcon icon="external-link-alt" className="url-icon" />
          {this.props.activity.organization.url.length < 100 ? (
            `${this.props.activity.organization.url}`
          ) : (
            <Translate contentKey="multiRecordView.goToWebsite" />
          )}
        </a>
      );
    }

    const content =
      type === 'checkbox' ? (
        <FormGroup check>
          {input}
          {label}
          {icon}
          {tooltip}
        </FormGroup>
      ) : (
        <FormGroup>
          {label}
          <Row className="input-field-container">
            <Col>{input}</Col>
            {urlButton}
            {icon}
            {tooltip}
            {clipboardButton}
            {clipboardTooltip}
          </Row>
        </FormGroup>
      );
    return content;
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InputField);
