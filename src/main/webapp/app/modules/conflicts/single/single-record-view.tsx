import 'filepond/dist/filepond.min.css';
import './single-record-view.scss';

import React from 'react';
import { connect } from 'react-redux';
import { Row, Col } from 'reactstrap';
import Tabs from './tabs';
import { getActivityDetails } from './single-record-view.reducer';
import { RouteComponentProps } from 'react-router-dom';

export interface ISingleRecordViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {}

export interface ISingleRecordViewState {
  match: any;
}

export class SingleRecordView extends React.Component<ISingleRecordViewProp, ISingleRecordViewState> {
  state: ISingleRecordViewState = {
    match: null
  };

  componentDidMount() {
    this.props.getActivityDetails(this.props.orgId);
  }

  render() {
    const { activityDetails } = this.props;
    const content = activityDetails ? (
      <Row>
        <Col>
          <h2>{activityDetails.record.organization.name}</h2>
          <Tabs activity={activityDetails} {...this.props} />
        </Col>
      </Row>
    ) : (
      <Row>
        <Col>
          <h2>Loading...</h2>
        </Col>
      </Row>
    );

    return content;
  }
}

const mapStateToProps = (storeState, { match }: ISingleRecordViewState) => ({
  orgId: match.params.orgId,
  activityDetails: storeState.singleRecordView.activityDetails
});

const mapDispatchToProps = { getActivityDetails };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SingleRecordView);
