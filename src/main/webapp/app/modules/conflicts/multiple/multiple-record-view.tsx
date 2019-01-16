import 'filepond/dist/filepond.min.css';
import './multiple-record-view.scss';

import React from 'react';
import { connect } from 'react-redux';
import { Row, Col, Jumbotron, Button } from 'reactstrap';
import Details from './components/details';
import { getBaseOrganization, getPartnerOrganization, getMatches } from './multiple-record-view.reducer';
import { RouteComponentProps } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

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
    this.props.getBaseOrganization(this.props.orgId);
    Promise.all([this.props.getMatches(this.props.orgId)]).then(() => {
      if (this.props.matches.length >= this.state.matchNumber + 1) {
        this.props.getPartnerOrganization(this.props.matches[this.state.matchNumber].partnerVersionId);
      }
    });
  }

  changeRecord = () => {
    let matchNumber = 0;
    if (this.state.matchNumber !== this.props.matches.length - 1) {
      matchNumber = this.state.matchNumber + 1;
    }
    this.setState({ matchNumber });
    this.props.getPartnerOrganization(this.props.matches[matchNumber].partnerVersionId);
  };

  render() {
    const { baseOrganization, partnerOrganization } = this.props;
    const loading = (
      <Col>
        <h2>Loading...</h2>
      </Col>
    );

    return (
      <div>
        <Row>
          {baseOrganization ? (
            <Col sm="6">
              <h2>{baseOrganization.name}</h2>
              <h4 className="from">
                <Translate contentKey="multiRecordView.yourData" />
              </h4>
              <Details organization={baseOrganization} {...this.props} />
            </Col>
          ) : (
            loading
          )}
          {partnerOrganization ? (
            <Col sm="6">
              <Row>
                <Col>
                  <h2>{partnerOrganization.name}</h2>
                  <h4 className="from">
                    <Translate contentKey="multiRecordView.from" />
                    {partnerOrganization.accountName}
                  </h4>
                </Col>
                <Col className="another-match-container" onClick={this.changeRecord}>
                  <h4 className="another-match-text">
                    <Translate contentKey="multiRecordView.seeAnotherMatch" />
                  </h4>
                  <FontAwesomeIcon className="another-match-icon" icon="angle-right" size="2x" />
                </Col>
              </Row>
              <Details organization={partnerOrganization} {...this.props} />
              <Jumbotron className="same-record-question-container">
                <div className="same-record-question">
                  <h4>
                    <Translate contentKey="multiRecordView.sameRecord.question" />
                  </h4>
                </div>
                <div className="same-record-question-buttons">
                  <Button color="success" size="lg">
                    <Translate contentKey="multiRecordView.sameRecord.confirm" />
                  </Button>
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
  baseOrganization: storeState.multipleRecordView.baseOrganization,
  partnerOrganization: storeState.multipleRecordView.partnerOrganization,
  matches: storeState.multipleRecordView.matches
});

const mapDispatchToProps = { getBaseOrganization, getPartnerOrganization, getMatches };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MultipleRecordView);
