import React from 'react';
import { FormGroup, Label, Input } from 'reactstrap';
import { connect } from 'react-redux';
import { Translate } from 'react-jhipster';

export interface IFieldProp extends StateProps, DispatchProps {
  entityClass: string;
  fieldName: string;
  type: string;
  defaultValue: any;
}

export class Field extends React.Component<IFieldProp> {
  getIdentifierName(entityClass, fieldName) {
    return entityClass.charAt(0).toLowerCase() + entityClass.slice(1) + fieldName.charAt(0).toUpperCase() + fieldName.slice(1);
  }

  render() {
    const { entityClass, fieldName, type, defaultValue } = this.props;
    const identifier = this.getIdentifierName(entityClass, fieldName);

    const input = <Input disabled type={type} name={identifier} id={identifier} value={defaultValue ? defaultValue : ''} />;
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
        </FormGroup>
      ) : (
        <FormGroup>
          {label}
          {input}
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
