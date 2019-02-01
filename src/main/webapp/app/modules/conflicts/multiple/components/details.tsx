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
  exclusions: any[];
}

export class Details extends React.Component<IMultipleRecordViewProp> {
  render() {
    const { organization, exclusions } = this.props;
    return (
      <div>
        <h4 className="orgDetailsTitle">
          <Translate contentKey="singleRecordView.details.title" />
        </h4>
        <Form>
          <Field entityClass="Organization" type="text" fieldName="name" defaultValue={organization.name} exclusions={exclusions} />
          <Field
            entityClass="Organization"
            type="text"
            fieldName="alternateName"
            defaultValue={organization.alternateName}
            exclusions={exclusions}
          />
          <Field
            entityClass="Organization"
            type="textarea"
            fieldName="description"
            defaultValue={organization.description}
            exclusions={exclusions}
          />
          <Field entityClass="Organization" type="text" fieldName="email" defaultValue={organization.email} exclusions={exclusions} />
          <Field entityClass="Organization" type="text" fieldName="url" defaultValue={organization.url} exclusions={exclusions} />
          <Field
            entityClass="Organization"
            type="text"
            fieldName="taxStatus"
            defaultValue={organization.taxStatus}
            exclusions={exclusions}
          />
          <Field entityClass="Organization" type="checkbox" fieldName="active" defaultValue={organization.active} exclusions={exclusions} />
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
