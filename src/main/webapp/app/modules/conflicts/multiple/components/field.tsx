import React from 'react';
import { FormGroup, Label, Input, Tooltip, Row, Col } from 'reactstrap';
import { connect } from 'react-redux';
import { Translate } from 'react-jhipster';
import classnames from 'classnames';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const DOMAIN_CLASS = 'org.benetech.servicenet.domain';

export interface IFieldProp extends StateProps, DispatchProps {
  entityClass: string;
  fieldName: string;
  type: string;
  defaultValue: any;
  exclusions: any[];
}

export interface IInputFieldState {
  tooltipOpen: boolean;
  isExcluded: boolean;
}

export class Field extends React.Component<IFieldProp, IInputFieldState> {
  state: IInputFieldState = {
    tooltipOpen: false,
    isExcluded: this.isExcludedField(this.props.fieldName)
  };

  getIdentifierName(entityClass, fieldName) {
    return entityClass.charAt(0).toLowerCase() + entityClass.slice(1) + fieldName.charAt(0).toUpperCase() + fieldName.slice(1);
  }

  isExcludedField(fieldName) {
    const entityPath = DOMAIN_CLASS + '.' + this.props.entityClass;
    return this.isExcluded(fieldName, entityPath);
  }

  isExcluded(fieldName, entityPath) {
    return this.props.exclusions.some(
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

  render() {
    const { entityClass, fieldName, type, defaultValue } = this.props;
    const identifier = this.getIdentifierName(entityClass, fieldName);
    let icon = null;
    if (this.state.isExcluded) {
      icon = (
        <div id={`${identifier}-icon`}>
          <FontAwesomeIcon className={`icon-excluded icon-${type}`} size="lg" icon="times-circle" />
        </div>
      );
    }

    let tooltip = null;
    if (this.state.isExcluded) {
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

    const input = (
      <Input
        disabled
        className={classnames({ grayBorder: this.state.isExcluded })}
        type={type}
        name={identifier}
        id={identifier}
        value={defaultValue ? defaultValue : ''}
      />
    );
    const label = (
      <Label for={identifier}>
        <Translate contentKey={`singleRecordView.inputField.${identifier}`} />
      </Label>
    );

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
)(Field);
