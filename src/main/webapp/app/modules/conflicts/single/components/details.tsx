import React from 'react';
import { Col, Row, Form, FormGroup, Label, Input } from 'reactstrap';
import './../single-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';

export interface ISingleRecordViewProp extends StateProps, DispatchProps {
  activity: IActivity;
}

export interface ISingleRecordViewState {
  activeTab: string;
}

export class Details extends React.Component<ISingleRecordViewProp, ISingleRecordViewState> {
  state: ISingleRecordViewState = {
    activeTab: '1'
  };

  toggle(tab) {
    if (this.state.activeTab !== tab) {
      this.setState({
        activeTab: tab
      });
    }
  }

  render() {
    const { activity } = this.props;

    return (
      <Row>
        <Col sm="6">
          <h4 className="orgDetailsTitle">
            <Translate contentKey="singleRecordView.details.title" />
          </h4>
          <Form>
            <FormGroup>
              <Label for="orgName">
                <Translate contentKey="singleRecordView.details.orgName" />
              </Label>
              <Input
                type="text"
                name="orgName"
                id="orgName"
                placeholder="The primary name of the organization"
                value={activity.organization.name}
              />
            </FormGroup>
            <FormGroup>
              <Label for="orgAlternateName">
                <Translate contentKey="singleRecordView.details.orgAlternateName" />
              </Label>
              <Input
                type="text"
                name="orgAlternateName"
                id="orgAlternateName"
                placeholder="The alternate name of the organization"
                value={activity.organization.alternateName}
              />
            </FormGroup>
            <FormGroup>
              <Label for="orgDescription">
                <Translate contentKey="singleRecordView.details.orgDescription" />
              </Label>
              <Input
                type="textarea"
                name="orgDescription"
                id="orgDescription"
                placeholder="The organizations description"
                value={activity.organization.description}
              />
            </FormGroup>
            <FormGroup>
              <Label for="orgEmail">
                <Translate contentKey="singleRecordView.details.orgEmail" />
              </Label>
              <Input type="text" name="orgEmail" id="orgEmail" placeholder="The organizations email" value={activity.organization.email} />
            </FormGroup>
            <FormGroup>
              <Label for="orgUrl">
                <Translate contentKey="singleRecordView.details.orgUrl" />
              </Label>
              <Input
                type="text"
                name="orgUrl"
                id="orgUrl"
                placeholder="The organizations website address"
                value={activity.organization.url}
              />
            </FormGroup>
            <FormGroup>
              <Label for="orgTaxStatus">
                <Translate contentKey="singleRecordView.details.orgTaxStatus" />
              </Label>
              <Input
                type="text"
                name="orgTaxStatus"
                id="orgTaxStatus"
                placeholder="Tax status of the organization"
                value={activity.organization.taxStatus}
              />
            </FormGroup>
            <FormGroup check>
              <Input type="checkbox" name="orgActive" id="orgActive" checked={activity.organization.active} />
              <Label for="orgActive" check>
                <Translate contentKey="singleRecordView.details.orgActive" />
              </Label>
            </FormGroup>
          </Form>
        </Col>
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
)(Details);
