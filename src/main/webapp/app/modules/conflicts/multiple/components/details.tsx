import React from 'react';
import { Form } from 'reactstrap';
import '../multiple-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import Field from './field';
import { RouteComponentProps } from 'react-router-dom';
import { IOrganization } from 'app/shared/model/organization.model';

export interface IMultipleRecordViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {
  organization: IOrganization;
}

export class Details extends React.Component<IMultipleRecordViewProp> {
  render() {
    const { organization } = this.props;
    return (
      <div>
        <h4 className="orgDetailsTitle">
          <Translate contentKey="singleRecordView.details.title" />
        </h4>
        <Form>
          <Field entityClass="Organization" type="text" fieldName="name" defaultValue={organization.name} />
          <Field entityClass="Organization" type="text" fieldName="alternateName" defaultValue={organization.alternateName} />
          <Field entityClass="Organization" type="textarea" fieldName="description" defaultValue={organization.description} />
          <Field entityClass="Organization" type="text" fieldName="email" defaultValue={organization.email} />
          <Field entityClass="Organization" type="text" fieldName="url" defaultValue={organization.url} />
          <Field entityClass="Organization" type="text" fieldName="taxStatus" defaultValue={organization.taxStatus} />
          <Field entityClass="Organization" type="checkbox" fieldName="active" defaultValue={organization.active} />
        </Form>
      </div>
    );
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Details);
