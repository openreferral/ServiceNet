import React from 'react';
import { Col, Row, FormGroup, Label, Input, Tooltip } from 'reactstrap';
import classnames from 'classnames';
import { connect } from 'react-redux';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IActivity } from 'app/shared/model/activity.model';

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

  getSuggestedValue(fieldName) {
    if (this.state.isConflicting) {
      return this.props.activity.conflicts.filter(e => e.fieldName === fieldName)[0].offeredValue;
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

  showTooltip = () => {
    if (this.state.isConflicting) {
      this.setState({
        tooltipOpen: true
      });
    }
  };

  hideTooltip = () => {
    this.setState({
      tooltipOpen: false
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
    const icon = this.state.isConflicting ? <FontAwesomeIcon className={`icon icon-${type}`} size="lg" icon="question-circle" /> : null;
    const tooltip = (
      <Tooltip placement="right" isOpen={this.state.tooltipOpen} target={identifier}>
        <Translate contentKey="singleRecordView.inputField.suggestedValue" />
        {this.getSuggestedValue(fieldName)}
      </Tooltip>
    );

    const content =
      type === 'checkbox' ? (
        <FormGroup check onMouseEnter={this.showTooltip} onMouseLeave={this.hideTooltip}>
          {input}
          {label}
          {icon}
          {tooltip}
        </FormGroup>
      ) : (
        <FormGroup>
          {label}
          <Row onMouseEnter={this.showTooltip} onMouseLeave={this.hideTooltip}>
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
