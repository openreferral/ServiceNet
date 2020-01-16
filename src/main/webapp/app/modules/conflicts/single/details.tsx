import React from 'react';
import './single-record-view.scss';
import { connect } from 'react-redux';
import { RouteComponentProps, Link } from 'react-router-dom';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { IOrganizationMatch } from 'app/shared/model//organization-match.model';
import { OrganizationDetails } from '../shared/components/organization-details';
import { LocationsDetails } from '../shared/components/location/locations-details';
import { ServicesDetails } from '../shared/components/service/services-details';
import { ContactsDetails } from '../shared/components/contact/contacts-details';
import { Col, Jumbotron } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import ReactGA from 'react-ga';

export interface ISingleRecordViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {
  activity: IActivityRecord;
  organizationMatches?: IOrganizationMatch[];
  dismissedMatches?: IOrganizationMatch[];
  isBaseRecord: boolean;
  showClipboard: boolean;
  orgId: string;
}

export interface ISingleRecordViewState {
  activeTab: string;
  isAreaOpen: boolean;
}

export class Details extends React.Component<ISingleRecordViewProp, ISingleRecordViewState> {
  state: ISingleRecordViewState = {
    activeTab: '1',
    isAreaOpen: false
  };

  toggle = tab => {
    if (this.state.activeTab !== tab) {
      this.setState({
        activeTab: tab
      });
    }
  };

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  handleMatchClick = () => {
    ReactGA.event({ category: 'UserActions', action: 'Clicking On Side By Side View' });
  };

  render() {
    const { orgId, organizationMatches, dismissedMatches } = this.props;
    const firstMatch = organizationMatches.length !== 0 ? organizationMatches[0] : null;

    const sideSection = firstMatch ? (
      <Col sm="3">
        <Jumbotron className="jumbotron">
          <div className="jumbotron-header">
            <h4 className="jumbotron-header-title">
              <Translate contentKey="singleRecordView.details.compare.title" />
            </h4>
            <p className="jumbotron-header-text">
              {organizationMatches.length}
              {organizationMatches.length === 1 ? (
                <Translate contentKey="singleRecordView.details.compare.singleHeader" />
              ) : (
                <Translate contentKey="singleRecordView.details.compare.multiHeader" />
              )}
            </p>
          </div>
          <div className="jumbotron-content">
            {organizationMatches
              ? organizationMatches
                  .sort((om1, om2) => Object.keys(om2.locationMatches).length - Object.keys(om1.locationMatches).length)
                  .map(match => (
                    <Link
                      to={`/multi-record-view/${match.organizationRecordId}/${match.partnerVersionId}`}
                      onClick={this.handleMatchClick}
                      className="match"
                    >
                      <h5>{match.partnerVersionName}</h5>
                      <div className="match-details">
                        <div>{match.providerName}</div>
                        <div className="text-right">
                          {match.numberOfLocations}{' '}
                          <Translate
                            contentKey={'singleRecordView.details.compare.' + (match.numberOfLocations > 1 ? 'locations' : 'location')}
                          />{' '}
                          {' / '}
                          {Object.keys(match.locationMatches).length}{' '}
                          <Translate
                            contentKey={
                              'singleRecordView.details.compare.' + (Object.keys(match.locationMatches).length > 1 ? 'matches' : 'match')
                            }
                          />
                        </div>
                      </div>
                    </Link>
                  ))
              : null}
            <Link to={`/all-records-view/${orgId}`} onClick={this.handleMatchClick} className="match">
              <h5>Show all matches</h5>
            </Link>
          </div>
        </Jumbotron>
      </Col>
    ) : !dismissedMatches.length ? null : (
      <div>
        <Link to={`/dismissed-matches/${this.props.orgId}`}>
          <Translate contentKey="singleRecordView.details.compare.viewDismissedMatches" />
        </Link>
      </div>
    );

    const columnSize = 6;
    const { isAreaOpen } = this.state;

    return (
      <div>
        <OrganizationDetails {...this.props} sideSection={sideSection} columnSize={columnSize} />
        <h5 className="expandBtn">
          <div className="collapseBtn" onClick={this.toggleAreaOpen}>
            <div className="collapseIcon">
              <FontAwesomeIcon size="xs" icon={isAreaOpen ? 'angle-up' : 'angle-down'} />
            </div>
            <Translate contentKey={isAreaOpen ? 'singleRecordView.details.collapseAll' : 'singleRecordView.details.expandAll'} />
          </div>
        </h5>
        {isAreaOpen ? (
          <div>
            <LocationsDetails {...this.props} locations={this.props.activity.locations} columnSize={columnSize} isAreaOpen />
            <ServicesDetails {...this.props} services={this.props.activity.services} columnSize={columnSize} isAreaOpen />
            <ContactsDetails {...this.props} contacts={this.props.activity.contacts} columnSize={columnSize} isAreaOpen />
          </div>
        ) : null}
        {isAreaOpen ? null : (
          <div>
            <LocationsDetails {...this.props} locations={this.props.activity.locations} columnSize={columnSize} isAreaOpen={false} />
            <ServicesDetails {...this.props} services={this.props.activity.services} columnSize={columnSize} isAreaOpen={false} />
            <ContactsDetails {...this.props} contacts={this.props.activity.contacts} columnSize={columnSize} isAreaOpen={false} />
          </div>
        )}
      </div>
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
