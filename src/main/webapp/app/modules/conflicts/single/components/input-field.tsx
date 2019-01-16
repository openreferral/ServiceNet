import React from 'react';
import { Col, Row, FormGroup, Label, Input, Tooltip } from 'reactstrap';
import classnames from 'classnames';
import { connect } from 'react-redux';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IActivity } from 'app/shared/model/activity.model';
import '../single-record-view.scss';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

const DOMAIN_CLASS = 'org.benetech.servicenet.domain';

export interface IInputFieldProp extends StateProps, DispatchProps {
  activity: IActivity;
  entityClass: string;
  fieldName: string;
  type: string;
  defaultValue: any;
}

export interface IInputFieldState {
  tooltipOpen: boolean;
  isConflicting: boolean;
}

export class InputField extends React.Component<IInputFieldProp, IInputFieldState> {
  state: IInputFieldState = {
    tooltipOpen: false,
    isConflicting: this.isConflictingField(this.props.fieldName)
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

  isConflicting(fieldName, entityPath) {
    return this.props.activity.conflicts.some(e => e.fieldName === fieldName && e.entityPath === entityPath);
  }

  toggleTooltip = () => {
    this.setState({
      tooltipOpen: !this.state.tooltipOpen
    });
  };

  getIdentifierName(entityClass, fieldName) {
    return entityClass.charAt(0).toLowerCase() + entityClass.slice(1) + fieldName.charAt(0).toUpperCase() + fieldName.slice(1);
  }

  render() {
    const { entityClass, fieldName, type, defaultValue } = this.props;
    const identifier = this.getIdentifierName(entityClass, fieldName);

    const input = (
      <Input
        disabled
        className={classnames({ blueBorder: this.state.isConflicting })}
        type={type}
        name={identifier}
        id={identifier}
        defaultValue={defaultValue}
      />
    );
    const label = (
      <Label for={identifier}>
        <Translate contentKey={`singleRecordView.inputField.${identifier}`} />
      </Label>
    );
    const icon = this.state.isConflicting ? (
      <div id={`${identifier}-icon`}>
        <FontAwesomeIcon className={`icon icon-${type}`} size="lg" icon="question-circle" />
      </div>
    ) : null;

    const suggestedValues = this.getSuggestedValues(fieldName);
    let partnersNumber = 0;
    if (suggestedValues) {
      suggestedValues.forEach(v => {
        partnersNumber += v.acceptedThisChange.length;
      });
    }
    const tooltip = this.state.isConflicting ? (
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
            {value.acceptedThisChange.map(accepted => (
              <div key={`accepted-${fieldName + accepted.name}`}>
                <p className="secondary">
                  {accepted.name}
                  <Translate contentKey="singleRecordView.inputField.imported" />
                  <TextFormat value={value.offeredValueDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
                </p>
              </div>
            ))}
          </div>
        ))}
      </Tooltip>
    ) : null;

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
          <Row>
            <Col>{input}</Col>
            {icon}
            {tooltip}
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
