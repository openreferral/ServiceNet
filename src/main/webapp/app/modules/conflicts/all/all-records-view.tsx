import 'filepond/dist/filepond.min.css';
import './all-records-view.scss';

import React from 'react';
import { connect } from 'react-redux';
import { Row, Col, Jumbotron, Button, Tooltip } from 'reactstrap';
import Details from '../shared/components/details';
import { getBaseRecord, getPartnerRecords, getNotHiddenMatchesByOrg } from '../shared/shared-record-view.reducer';
import {
  getSystemAccountEntities as getSettings,
  updateSelectedSettings
} from 'app/entities/fields-display-settings/fields-display-settings.reducer';
import { RouteComponentProps, Link } from 'react-router-dom';
import { Translate, TextFormat, translate } from 'react-jhipster';
import ReactGA from 'react-ga';
import axios from 'axios';
import HideRecordButton from 'app/shared/layout/hide-record-button';
import { toast } from 'react-toastify';
import _ from 'lodash';
import Select from 'react-select';

import { APP_DATE_FORMAT } from 'app/config/constants';
import DismissModal from '../shared/components/dismiss-modal';
import SuccessModal from '../shared/components/success-modal';
import FieldsDisplaySettingsPanel from '../multiple/fields-display-settings-panel';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export interface IAllRecordsViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {}

export interface IAllRecordsViewState {
  match: any;
  matchNumber: number;
  showDismissModal: boolean;
  showSuccessModal: boolean;
  dismissError: boolean;
  locationMatches: any;
  selectedLocation: any;
  matchLocations: boolean;
  matchingLocation: any;
  selectedMatch: any;
  fieldSettingsExpanded: boolean;
  tooltipOpen: boolean;
  selectedSettings: any;
}

export class AllRecordsView extends React.Component<IAllRecordsViewProp, IAllRecordsViewState> {
  state: IAllRecordsViewState = {
    match: null,
    matchNumber: 0,
    showDismissModal: false,
    showSuccessModal: false,
    dismissError: false,
    locationMatches: [],
    selectedLocation: null,
    matchLocations: true,
    matchingLocation: null,
    selectedMatch: null,
    fieldSettingsExpanded: false,
    tooltipOpen: false,
    selectedSettings: {}
  };

  componentDidMount() {
    this.props.getBaseRecord(this.props.orgId);
    this.props.getSettings();
    Promise.all([this.props.getNotHiddenMatchesByOrg(this.props.orgId)]).then(() => {
      if (this.props.matches.length >= 0) {
        this.props.getPartnerRecords(this.props.orgId);
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
    const match = this.state.selectedMatch;
    const matchId = match && match.id ? match.id : '';
    axios
      .post(`/api/organization-matches/${matchId}/dismiss`, dismissParams)
      .then(() => {
        this.setState({ showDismissModal: false, showSuccessModal: true, matchNumber: 0 });
        Promise.all([this.props.getNotHiddenMatchesByOrg(this.props.orgId)]).then(() => {
          if (this.props.matches.length > 0) {
            this.props.getPartnerRecords(this.props.orgId);
          } else {
            this.props.history.push(`/single-record-view/${orgId}`);
          }
        });
      })
      .catch(() => {
        this.setState({ dismissError: true });
      });
  };

  showDismissModal = partnerRecordId => () => {
    const selectedMatch = _.find(this.props.matches, m => m.partnerVersionId === partnerRecordId);
    this.setState({
      showDismissModal: true,
      selectedMatch
    });
  };

  denyMatch = () => {
    ReactGA.event({ category: 'UserActions', action: 'Deny Match Button' });
  };

  hideActivity = partnerRecordId => event => {
    const { matches, orgId } = this.props;

    const match = _.find(this.props.matches, m => m.partnerVersionId === partnerRecordId);
    const matchId = match && match.id ? match.id : '';
    event.preventDefault();
    axios
      .post(`/api/organization-matches/${matchId}/hide`)
      .then(() => {
        toast.success(translate('hiddenMatches.hiddenSuccessfully'));
        if (matches.length === 1) {
          this.props.history.replace(`/single-record-view/${orgId}`);
        } else if (matches.length > 1) {
          window.location.reload();
        } else {
          this.props.history.push(`/`);
        }
      })
      .catch(() => {
        toast.error(translate('hiddenMatches.hidingError'));
      });
  };

  selectLocation = location => {
    if (!!location) {
      const selectedLocation = location.location.id;
      const matchingLocation = this.findMatchingLocation(selectedLocation);
      this.setState({
        selectedLocation,
        matchingLocation
      });
    }
  };

  findMatchingLocation = selectedLocation => {
    const { matches, partnerRecords } = this.props;
    const match = partnerRecords.length && _.find(matches, m => m.partnerVersionId === partnerRecords[0].organization.id);
    if (match && match.locationMatches) {
      if (selectedLocation in match.locationMatches) {
        return match.locationMatches[selectedLocation][0];
      }
      // return inverted match if any
      return _.findKey(match.locationMatches, matchList => matchList.includes(selectedLocation));
    }
  };

  toggleMatchLocations = () => {
    this.setState({
      matchLocations: !this.state.matchLocations
    });
  };

  toggleFieldSettings = () => {
    if (this.state.fieldSettingsExpanded) {
      this.props.getSettings();
    }
    this.setState({
      fieldSettingsExpanded: !this.state.fieldSettingsExpanded
    });
  };

  handleSettingsChange = selectedSettings => {
    this.setState({ selectedSettings });
    this.props.updateSelectedSettings(selectedSettings);
  };

  toggleTooltip = () => {
    this.setState({
      tooltipOpen: !this.state.tooltipOpen
    });
  };

  render() {
    const { baseRecord, partnerRecords, systemAccountName, matches } = this.props;
    const match = partnerRecords.length && _.find(matches, m => m.partnerVersionId === partnerRecords[0].organization.id);
    const baseProviderName = baseRecord ? baseRecord.organization.accountName : null;
    const loading = (
      <Col>
        <h2>Loading...</h2>
      </Col>
    );

    let pageBody = null;
    if (this.state.fieldSettingsExpanded) {
      pageBody = (
        <Row>
          <Col md="12">
            <FieldsDisplaySettingsPanel />
          </Col>
        </Row>
      );
    } else {
      pageBody = (
        <Row className="row flex-row flex-nowrap">
          {baseRecord ? (
            <div className="record">
              <h2>{baseRecord.organization.name}</h2>
              <h4 className="from">
                {systemAccountName === baseProviderName ? (
                  <Translate contentKey="multiRecordView.yourData" />
                ) : (
                  <div>
                    <Translate contentKey="multiRecordView.from" />
                    {baseProviderName}
                  </div>
                )}
              </h4>
              <h5>
                <Translate contentKey="multiRecordView.lastCompleteReview" />
                {baseRecord.organization.lastVerifiedOn ? (
                  <TextFormat value={baseRecord.organization.lastVerifiedOn} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                ) : (
                  <Translate contentKey="multiRecordView.unknown" />
                )}
              </h5>
              <h5>
                <Translate contentKey="multiRecordView.lastUpdated" />
                {baseRecord.lastUpdated ? (
                  <TextFormat value={baseRecord.lastUpdated} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                ) : (
                  <Translate contentKey="multiRecordView.unknown" />
                )}
              </h5>
              <Details
                activity={baseRecord}
                {...this.props}
                exclusions={baseRecord.exclusions}
                isBaseRecord
                showClipboard={false}
                selectLocation={this.selectLocation}
                matchLocations={this.state.matchLocations}
                matchingLocation={this.state.matchingLocation}
                toggleMatchLocations={this.toggleMatchLocations}
                settings={this.props.selectedSettings}
                serviceMatches={match.serviceMatches}
                locationMatches={match.locationMatches}
              />
            </div>
          ) : (
            loading
          )}
          <Col className="partner-records row flex-row flex-nowrap">
            {partnerRecords.map((partnerRecord, idx) => (
              <div className="record flex-grow-1">
                <Row>
                  <Col>
                    <div style={{ float: 'right' }}>
                      <HideRecordButton
                        id={`hide-${partnerRecord.organization.id}`}
                        handleHide={this.hideActivity(partnerRecord.organization.id)}
                      />
                    </div>
                    <div style={{ float: 'right', marginTop: '30px' }}>
                      <h5>
                        <Translate contentKey="multiRecordView.matchSimilarity" />
                        {matches[idx] ? (matches[idx].similarity * 100).toFixed(2) : 0}%
                      </h5>
                    </div>
                    <h2 className="mr-4">{partnerRecord.organization.name}</h2>
                    <h4 className="from">
                      <Translate contentKey="multiRecordView.from" />
                      {partnerRecord.organization.accountName}
                    </h4>
                    <h5>
                      <Translate contentKey="multiRecordView.lastCompleteReview" />
                      {partnerRecord.organization.lastVerifiedOn ? (
                        <TextFormat value={partnerRecord.organization.lastVerifiedOn} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                      ) : (
                        <Translate contentKey="multiRecordView.unknown" />
                      )}
                    </h5>
                    <h5>
                      <Translate contentKey="multiRecordView.lastUpdated" />
                      {partnerRecord.lastUpdated ? (
                        <TextFormat value={partnerRecord.lastUpdated} type="date" format={APP_DATE_FORMAT} blankOnInvalid />
                      ) : (
                        <Translate contentKey="multiRecordView.unknown" />
                      )}
                    </h5>
                  </Col>
                </Row>
                <Details
                  activity={partnerRecord}
                  {...this.props}
                  exclusions={[]}
                  isBaseRecord={false}
                  showClipboard
                  selectLocation={this.selectLocation}
                  matchLocations={this.state.matchLocations}
                  matchingLocation={this.state.matchingLocation}
                  settings={this.props.selectedSettings}
                  serviceMatches={match.serviceMatches}
                  locationMatches={match.locationMatches}
                />
                <Jumbotron className="same-record-question-container">
                  <div className="text-center">
                    <h4>
                      <Translate contentKey="multiRecordView.sameRecord.question" />
                    </h4>
                  </div>
                  <div className="d-flex justify-content-around">
                    <div>
                      {!this.props.dismissedMatches.length ? null : (
                        <Link to={`/dismissed-matches/${this.props.orgId}`}>
                          <Translate contentKey="multiRecordView.sameRecord.viewDismissedMatches" />
                        </Link>
                      )}
                    </div>
                    <Button color="danger" size="lg" onClick={this.showDismissModal(partnerRecord.organization.id)}>
                      <Translate contentKey="multiRecordView.sameRecord.deny" />
                    </Button>
                  </div>
                </Jumbotron>
              </div>
            ))}
          </Col>
        </Row>
      );
    }

    return (
      <div>
        <SuccessModal showModal={this.state.showSuccessModal} handleClose={this.handleSuccessModalClose} />
        <DismissModal
          showModal={this.state.showDismissModal}
          dismissError={this.state.dismissError}
          handleClose={this.handleDismissModalClose}
          handleDismiss={this.handleDismiss}
        />
        <div className="fields-display-settings-btn" onClick={this.toggleFieldSettings} id="fields-display-settings-btn">
          {this.state.fieldSettingsExpanded ? (
            <FontAwesomeIcon icon="undo-alt" size="lg" />
          ) : (
            <span>
              <Translate contentKey="multiRecordView.showLessFields" /> <FontAwesomeIcon icon="cogs" size="lg" />
            </span>
          )}
        </div>
        <Select
          options={this.props.fieldsDisplaySettingsOptions}
          onChange={this.handleSettingsChange}
          value={this.props.selectedSettings}
          className="fields-display-settings-selector"
          isDisabled={this.state.fieldSettingsExpanded}
        />
        <Tooltip
          placement="bottom"
          innerClassName="tooltip-clip-inner"
          className="tooltip-clip"
          isOpen={this.state.tooltipOpen}
          target="fields-display-settings-btn"
          toggle={this.toggleTooltip}
          autohide
        >
          <Translate contentKey="global.menu.entities.fieldsDisplaySettings" />
        </Tooltip>
        {pageBody}
      </div>
    );
  }
}

const mapStateToProps = (storeState, { match }: IAllRecordsViewState) => ({
  orgId: match.params.orgId,
  baseRecord: storeState.sharedRecordView.baseRecord,
  partnerRecords: storeState.sharedRecordView.partnerRecords,
  matches: storeState.sharedRecordView.matches,
  dismissedMatches: storeState.sharedRecordView.dismissedMatches,
  systemAccountName: storeState.authentication.account.systemAccountName,
  selectedSettings: storeState.fieldsDisplaySettings.selectedSettings,
  fieldsDisplaySettingsOptions: _.union(
    [{ value: null, label: 'All fields' }],
    storeState.fieldsDisplaySettings.entities.map(o => ({ ...o, value: o.id, label: o.name }))
  )
});

const mapDispatchToProps = { getBaseRecord, getPartnerRecords, getNotHiddenMatchesByOrg, getSettings, updateSelectedSettings };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AllRecordsView);
