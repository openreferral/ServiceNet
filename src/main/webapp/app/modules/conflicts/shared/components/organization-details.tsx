import React from 'react';
import { Col, Row } from 'reactstrap';
import '../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { AdditionalDetails } from './additional-details';

export interface IOrganizationDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  sideSection: any;
  columnSize: number;
}

export class OrganizationDetails extends React.Component<IOrganizationDetailsProp> {
  getTextField = (organization, fieldName) => ({
    type: 'text',
    fieldName,
    defaultValue: organization[fieldName]
  });

  render() {
    const { columnSize, sideSection } = this.props;
    const { organization } = this.props.activity.record;

    const fields = [
      this.getTextField(organization, 'name'),
      this.getTextField(organization, 'alternateName'),
      {
        type: 'textarea',
        fieldName: 'description',
        defaultValue: organization.description
      },
      this.getTextField(organization, 'email'),
      this.getTextField(organization, 'url'),
      this.getTextField(organization, 'taxStatus'),
      {
        type: 'checkbox',
        fieldName: 'active',
        defaultValue: organization.active
      }
    ];

    return (
      <Row>
        <Col sm={columnSize}>
          <AdditionalDetails
            {...this.props}
            fields={fields}
            entityClass={'Organization'}
            customHeader={false}
            additionalFields={false}
            toggleAvailable={false}
            isCustomToggle={false}
            customToggleValue={false}
          />
        </Col>
        {sideSection}
      </Row>
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
)(OrganizationDetails);
