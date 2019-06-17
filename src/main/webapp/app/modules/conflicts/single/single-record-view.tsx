import 'filepond/dist/filepond.min.css';
import './single-record-view.scss';

import React from 'react';
import { connect } from 'react-redux';
import { Row, Col } from 'reactstrap';
import Tabs from './tabs';
import { getBaseRecord, getMatches } from '../shared/shared-record-view.reducer';
import { RouteComponentProps } from 'react-router-dom';

export interface ISingleRecordViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {
  showClipboard: boolean;
}

export interface ISingleRecordViewState {
  match: any;
}

export class SingleRecordView extends React.Component<ISingleRecordViewProp, ISingleRecordViewState> {
  state: ISingleRecordViewState = {
    match: null
  };

  componentDidMount() {
    this.props.getBaseRecord(this.props.orgId);
    this.props.getMatches(this.props.orgId);
  }

  render() {
    const { activityRecord } = this.props;
    const content = activityRecord ? (
      <Row>
        <Col>
          <h2>{activityRecord.organization.name}</h2>
          <Tabs activity={activityRecord} {...this.props} />
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
  activityRecord: storeState.sharedRecordView.baseRecord,
  organizationMatches: storeState.sharedRecordView.matches,
  dismissedMatches: storeState.sharedRecordView.dismissedMatches
});

const mapDispatchToProps = { getBaseRecord, getMatches };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SingleRecordView);
