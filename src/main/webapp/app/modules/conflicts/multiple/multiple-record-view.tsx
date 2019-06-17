import 'filepond/dist/filepond.min.css';
import './multiple-record-view.scss';

import React from 'react';
import { connect } from 'react-redux';
import { Row, Col, Jumbotron, Button } from 'reactstrap';
import Details from './components/details';
import { getBaseRecord, getPartnerRecord, getMatches } from '../shared/shared-record-view.reducer';
import { RouteComponentProps, Link } from 'react-router-dom';
import { Translate, TextFormat } from 'react-jhipster';
import ReactGA from 'react-ga';
import axios from 'axios';

import { APP_DATE_FORMAT } from 'app/config/constants';
import DismissModal from './components/dismiss-modal';
import SuccessModal from './components/success-modal';

export interface IMultipleRecordViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {}

export interface IMultipleRecordViewState {
  match: any;
  matchNumber: number;
  showDismissModal: boolean;
  showSuccessModal: boolean;
  dismissError: boolean;
}

export class MultipleRecordView extends React.Component<IMultipleRecordViewProp, IMultipleRecordViewState> {
  state: IMultipleRecordViewState = {
    match: null,
    matchNumber: 0,
    showDismissModal: false,
    showSuccessModal: false,
    dismissError: false
  };

  componentDidMount() {
    this.props.getBaseRecord(this.props.orgId);
    Promise.all([this.props.getMatches(this.props.orgId)]).then(() => {
      if (this.props.matches.length >= this.state.matchNumber + 1) {
        this.props.getPartnerRecord(this.props.matches[this.state.matchNumber].partnerVersionId);
      }
    });
  }

  handleDismissModalClose = () => {
    this.setState({ showDismissModal: false, dismissError: false });
  };

  handleSuccessModalClose = () => {
    this.setState({ showSuccessModal: false });
  };

  handleDismiss = dismissParams => {
    const { orgId } = this.props;
    const matchId = this.props.matches[this.state.matchNumber].id;
    axios
      .post(`/api/organization-matches/${matchId}/dismiss`, dismissParams)
      .then(() => {
        this.setState({ showDismissModal: false, showSuccessModal: true, matchNumber: 0 });
        Promise.all([this.props.getMatches(this.props.orgId)]).then(() => {
          if (this.props.matches.length > 0) {
            this.props.getPartnerRecord(this.props.matches[0].partnerVersionId);
          } else {
            this.props.history.push(`/single-record-view/${orgId}`);
          }
        });
      })
      .catch(() => {
        this.setState({ dismissError: true });
      });
  };

  showDismissModal = () => {
    this.setState({ showDismissModal: true });
  };

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
        <SuccessModal showModal={this.state.showSuccessModal} handleClose={this.handleSuccessModalClose} />
        <DismissModal
          showModal={this.state.showDismissModal}
          dismissError={this.state.dismissError}
          handleClose={this.handleDismissModalClose}
          handleDismiss={this.handleDismiss}
        />
        <Row>
          {baseRecord ? (
            <Col sm="6">
              <h2>{baseRecord.organization.name}</h2>
              <h4 className="from">
                <Translate contentKey="multiRecordView.yourData" />
              </h4>
              <h5>
                <Translate contentKey="multiRecordView.lastUpdated" />
                <TextFormat value={baseRecord.lastUpdated} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
              </h5>
              <Details activity={baseRecord} {...this.props} exclusions={baseRecord.exclusions} isBaseRecord showClipboard={false} />
            </Col>
          ) : (
            loading
          )}
          {partnerRecord ? (
            <Col sm="6">
              <Row>
                <Col>
                  <h2>{partnerRecord.organization.name}</h2>
                  <h4 className="from">
                    <Translate contentKey="multiRecordView.from" />
                    {partnerRecord.organization.accountName}
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
                  <div>
                    {!this.props.dismissedMatches.length ? null : (
                      <Link to={`/dismissed-matches/${this.props.orgId}`}>
                        <Translate contentKey="multiRecordView.sameRecord.viewDismissedMatches" />
                      </Link>
                    )}
                  </div>
                  <Button color="danger" size="lg" onClick={this.showDismissModal}>
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
  baseRecord: storeState.sharedRecordView.baseRecord,
  partnerRecord: storeState.sharedRecordView.partnerRecord,
  matches: storeState.sharedRecordView.matches,
  dismissedMatches: storeState.sharedRecordView.dismissedMatches
});

const mapDispatchToProps = { getBaseRecord, getPartnerRecord, getMatches };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MultipleRecordView);
