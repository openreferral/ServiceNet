import React from 'react';
import { Col, Row, Jumbotron } from 'reactstrap';
import '../single-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { IActivity } from 'app/shared/model/activity.model';
import { AdditionalDetails } from './additional-details';

export interface IOrganizationDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
}

export class OrganizationDetails extends React.Component<IOrganizationDetailsProp> {
  getTextField = (organization, fieldName) => ({
    type: 'text',
    fieldName,
    defaultValue: organization[fieldName]
  });

  render() {
    const { activity } = this.props;
    const { organization } = this.props.activity.record;
    const firstMatch = activity.organizationMatches.length !== 0 ? activity.organizationMatches[0] : null;

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
        <Col sm="6">
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
        {firstMatch ? (
          <Col sm="3">
            <Jumbotron className="jumbotron">
              <div className="jumbotron-header">
                <h4 className="jumbotron-header-title">
                  <Translate contentKey="singleRecordView.details.compare.title" />
                </h4>
                <p className="jumbotron-header-text">
                  {activity.organizationMatches.length}
                  {activity.organizationMatches.length === 1 ? (
                    <Translate contentKey="singleRecordView.details.compare.singleHeader" />
                  ) : (
                    <Translate contentKey="singleRecordView.details.compare.multiHeader" />
                  )}
                </p>
              </div>
              <div className="jumbotron-content">
                <p>
                  <Translate contentKey="singleRecordView.details.compare.content1" />
                </p>
                <p>
                  <Translate contentKey="singleRecordView.details.compare.content2" />
                </p>
                <p className="review-title">
                  <Translate contentKey="singleRecordView.details.compare.review" />
                </p>
                <div>
                  <Link to={`/multi-record-view/${firstMatch.organizationRecordId}`}>
                    {activity.organizationMatches.length === 1
                      ? `${firstMatch.partnerVersionName}`
                      : `${firstMatch.partnerVersionName} +${activity.organizationMatches.length - 1}`}
                  </Link>
                </div>
              </div>
            </Jumbotron>
          </Col>
        ) : null}
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
