import React from 'react';
import { Col, Row, Form, Button } from 'reactstrap';
import '../single-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import InputField from './input-field';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { RouteComponentProps } from 'react-router-dom';
import { updateOrganization } from '../single-record-view.reducer';
import { IActivity } from 'app/shared/model/activity.model';
import { IOrganization } from 'app/shared/model/organization.model';

export interface ISingleRecordViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {
  activity: IActivity;
}

export interface ISingleRecordViewState {
  activeTab: string;
  tooltipOpen: boolean;
  organization: IOrganization;
}

export class Details extends React.Component<ISingleRecordViewProp, ISingleRecordViewState> {
  state: ISingleRecordViewState = {
    activeTab: '1',
    tooltipOpen: false,
    organization: this.props.activity.organization
  };

  toggle = tab => {
    if (this.state.activeTab !== tab) {
      this.setState({
        activeTab: tab
      });
    }
  };

  save = () => {
    updateOrganization(this.state.organization);
    this.props.history.push('/');
  };

  handleChange = e => {
    const { name, value } = e;
    const updatedOrg = this.state.organization;
    updatedOrg[name] = value;
    this.setState({
      organization: updatedOrg
    });
  };

  render() {
    const { activity } = this.props;
    const { organization } = this.state;

    return (
      <Row>
        <Col sm="6">
          <h4 className="orgDetailsTitle">
            <Translate contentKey="singleRecordView.details.title" />
          </h4>
          <Form>
            <InputField
              entityClass="Organization"
              activity={activity}
              type="text"
              valueChange={this.handleChange}
              fieldName="name"
              defaultValue={organization.name}
            />
            <InputField
              entityClass="Organization"
              activity={activity}
              type="text"
              valueChange={this.handleChange}
              fieldName="alternateName"
              defaultValue={organization.alternateName}
            />
            <InputField
              entityClass="Organization"
              activity={activity}
              type="textarea"
              valueChange={this.handleChange}
              fieldName="description"
              defaultValue={organization.description}
            />
            <InputField
              entityClass="Organization"
              activity={activity}
              type="text"
              valueChange={this.handleChange}
              fieldName="email"
              defaultValue={organization.email}
            />
            <InputField
              entityClass="Organization"
              activity={activity}
              type="text"
              valueChange={this.handleChange}
              fieldName="url"
              defaultValue={organization.url}
            />
            <InputField
              entityClass="Organization"
              activity={activity}
              type="text"
              valueChange={this.handleChange}
              fieldName="taxStatus"
              defaultValue={organization.taxStatus}
            />
            <InputField
              entityClass="Organization"
              activity={activity}
              type="checkbox"
              valueChange={this.handleChange}
              fieldName="active"
              defaultValue={organization.active}
            />
          </Form>
          <Button color="primary" id="save" onClick={this.save}>
            <FontAwesomeIcon icon="save" />
            <Translate contentKey="entity.action.save" />
          </Button>
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
