import 'filepond/dist/filepond.min.css';
import './multiple-record-view.scss';

import React from 'react';
import { connect } from 'react-redux';
import { Row, Col, Jumbotron, Button } from 'reactstrap';
import Details from './components/details';
import { getBaseRecord, getPartnerRecord, getMatches } from './multiple-record-view.reducer';
import { RouteComponentProps } from 'react-router-dom';
import { Translate, TextFormat } from 'react-jhipster';
import ReactGA from 'react-ga';

import { APP_DATE_FORMAT } from 'app/config/constants';

export interface IMultipleRecordViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {}

export interface IMultipleRecordViewState {
  match: any;
  matchNumber: number;
}

export class MultipleRecordView extends React.Component<IMultipleRecordViewProp, IMultipleRecordViewState> {
  state: IMultipleRecordViewState = {
    match: null,
    matchNumber: 0
  };

  componentDidMount() {
    this.props.getBaseRecord(this.props.orgId);
    Promise.all([this.props.getMatches(this.props.orgId)]).then(() => {
      if (this.props.matches.length >= this.state.matchNumber + 1) {
        this.props.getPartnerRecord(this.props.matches[this.state.matchNumber].partnerVersionId);
      }
    });
  }

  changeRecord = offset => () => {
    let matchNumber = 0;
    const offsetMatchNumber = this.state.matchNumber + offset;
    if (offsetMatchNumber < 0) {
      matchNumber = this.props.matches.length - 1;
    } else if (offsetMatchNumber < this.props.matches.length) {
      matchNumber = offsetMatchNumber;
    }
    this.setState({ matchNumber });

    ReactGA.event({ category: 'UserActions', action: 'Clicking "See Another Match" on side by side view' });
    this.props.getPartnerRecord(this.props.matches[matchNumber].partnerVersionId);
  };

  confirmMatch = () => {
    ReactGA.event({ category: 'UserActions', action: 'Confirm Match Button' });
  };

  denyMatch = () => {
    ReactGA.event({ category: 'UserActions', action: 'Deny Match Button' });
  };

  render() {
    const { baseRecord, partnerRecord } = this.props;
    const loading = (
      <Col>
        <h2>Loading...</h2>
      </Col>
    );

    const seeAnotherMatch =
      this.props.matches.length > 1 ? (
        <Col>
          <h4>
            <span role="button" onClick={this.changeRecord(-1)} className="text-blue">
              〈
            </span>
            <span role="button" onClick={this.changeRecord(1)}>
              <Translate contentKey="multiRecordView.seeAnotherMatch" />
              <span className="text-blue">{` (${this.state.matchNumber + 1}/${this.props.matches.length}) 〉`}</span>
            </span>
          </h4>
        </Col>
      ) : null;

    return (
      <div>
        <Row>
          {baseRecord ? (
            <Col sm="6">
              <h2>{baseRecord.record.organization.name}</h2>
              <h4 className="from">
                <Translate contentKey="multiRecordView.yourData" />
              </h4>
              <h5>
                <Translate contentKey="multiRecordView.lastUpdated" />
                <TextFormat value={baseRecord.lastUpdated} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
              </h5>
              <Details activity={baseRecord} {...this.props} exclusions={baseRecord.record.exclusions} isBaseRecord showClipboard={false} />
            </Col>
          ) : (
            loading
          )}
          {partnerRecord ? (
            <Col sm="6">
              <Row>
                <Col>
                  <h2>{partnerRecord.record.organization.name}</h2>
                  <h4 className="from">
                    <Translate contentKey="multiRecordView.from" />
                    {partnerRecord.record.organization.accountName}
                  </h4>
                  <h5>
                    <Translate contentKey="multiRecordView.lastUpdated" />
                    <TextFormat value={partnerRecord.lastUpdated} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                  </h5>
                </Col>
                {seeAnotherMatch}
              </Row>
              <Details activity={partnerRecord} {...this.props} exclusions={[]} isBaseRecord={false} showClipboard />
              <Jumbotron className="same-record-question-container">
                <div className="same-record-question">
                  <h4>
                    <Translate contentKey="multiRecordView.sameRecord.question" />
                  </h4>
                </div>
                <div className="same-record-question-buttons">
                  <div />
                  <Button color="danger" size="lg">
                    <Translate contentKey="multiRecordView.sameRecord.deny" />
                  </Button>
                </div>
              </Jumbotron>
            </Col>
          ) : (
            loading
          )}
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState, { match }: IMultipleRecordViewState) => ({
  orgId: match.params.orgId,
  baseRecord: storeState.multipleRecordView.baseRecord,
  partnerRecord: storeState.multipleRecordView.partnerRecord,
  matches: storeState.multipleRecordView.matches
});

const mapDispatchToProps = { getBaseRecord, getPartnerRecord, getMatches };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MultipleRecordView);
