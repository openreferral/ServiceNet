import React from 'react';
import { Col, Row, Form, Jumbotron } from 'reactstrap';
import '../single-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import InputField from './input-field';
import { Link } from 'react-router-dom';
import { IActivity } from 'app/shared/model/activity.model';
import { IOrganization } from 'app/shared/model/organization.model';

export interface IOrganizationDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
}

export interface IOrganizationDetailsState {
  organization: IOrganization;
  entityClass: string;
}

export class OrganizationDetails extends React.Component<IOrganizationDetailsProp, IOrganizationDetailsState> {
  state: IOrganizationDetailsState = {
    organization: this.props.activity.record.organization,
    entityClass: 'Organization'
  };

  render() {
    const { activity } = this.props;
    const { organization } = this.state;
    const firstMatch = activity.organizationMatches.length !== 0 ? activity.organizationMatches[0] : null;

    return (
      <Row>
        <Col sm="6">
          <h4 className="title">
            <Translate contentKey="singleRecordView.details.organizationTitle" />
          </h4>
          <Form>
            <InputField {...this.props} {...this.state} type="text" fieldName="name" defaultValue={organization.name} />
            <InputField {...this.props} {...this.state} type="text" fieldName="alternateName" defaultValue={organization.alternateName} />
            <InputField {...this.props} {...this.state} type="textarea" fieldName="description" defaultValue={organization.description} />
            <InputField {...this.props} {...this.state} type="text" fieldName="email" defaultValue={organization.email} />
            <InputField {...this.props} {...this.state} type="text" fieldName="url" defaultValue={organization.url} />
            <InputField {...this.props} {...this.state} type="text" fieldName="taxStatus" defaultValue={organization.taxStatus} />
            <InputField {...this.props} {...this.state} type="checkbox" fieldName="active" defaultValue={organization.active} />
          </Form>
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
