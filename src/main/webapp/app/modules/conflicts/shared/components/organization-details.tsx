import React from 'react';
import { Col, Row } from 'reactstrap';
import '../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { AdditionalDetails } from './additional-details';
import _ from 'lodash';

export interface IOrganizationDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  sideSection: any;
  columnSize: number;
  showClipboard: boolean;
  settings?: any;
}

export class OrganizationDetails extends React.Component<IOrganizationDetailsProp> {
  getTextField = (organization, fieldName) => ({
    type: 'text',
    fieldName,
    defaultValue: organization[fieldName]
  });

  getFields = () => {
    const { settings } = this.props;
    const { organization } = this.props.activity;

    const fieldsMap = {
      NAME: this.getTextField(organization, 'name'),
      ALTERNATE_NAME: this.getTextField(organization, 'alternateName'),
      DESCRIPTION: {
        type: 'textarea',
        fieldName: 'description',
        defaultValue: organization.description
      },
      EMAIL: this.getTextField(organization, 'email'),
      URL: this.getTextField(organization, 'url'),
      TAX_STATUS: this.getTextField(organization, 'taxStatus'),
      ACTIVE: {
        type: 'checkbox',
        fieldName: 'active',
        defaultValue: organization.active
      }
    };

    if (settings === undefined || (!!settings && !settings.id)) {
      return _.values(fieldsMap);
    }

    const { organizationFields } = settings;

    const fieldsMapKeys = _.keys(fieldsMap);
    const keysFiltered = _.filter(fieldsMapKeys, k => organizationFields.indexOf(k) > -1);
    return _.values(_.pick(fieldsMap, keysFiltered));
  };

  render() {
    const { columnSize, sideSection, showClipboard } = this.props;

    return (
      <Row>
        <Col sm={columnSize}>
          <AdditionalDetails
            {...this.props}
            fields={this.getFields()}
            entityClass={'Organization'}
            customHeader={false}
            additionalFields={false}
            toggleAvailable={false}
            isCustomToggle={false}
            customToggleValue={false}
            showClipboard={showClipboard}
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
