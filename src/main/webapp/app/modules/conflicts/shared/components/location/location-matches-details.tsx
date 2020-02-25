import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { Collapse } from 'reactstrap';
import _ from 'lodash';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate } from 'react-jhipster';
import { getPartnerRecord } from '../../shared-record-view.reducer';
import { Link } from 'react-router-dom';
import ReactGA from 'react-ga';

export interface ILocationMatch {
  id: string;
  organizationName: string;
  locationName: string;
  locationId: string;
  matchingLocationId: string;
  orgId: string;
}

export interface ILocationMatchesDetailsProp extends StateProps, DispatchProps {
  locationMatches: ILocationMatch;
  locationId: any;
  isBaseRecord: boolean;
  orgId: string;
}

export interface ILocationMatchesDetailsState {
  isAreaOpen: boolean;
}

export class LocationMatchesDetails extends React.Component<ILocationMatchesDetailsProp, ILocationMatchesDetailsState> {
  state: ILocationMatchesDetailsState = {
    isAreaOpen: true
  };

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  handleMatchClick = orgId => () => {
    this.props.getPartnerRecord(orgId);
    ReactGA.event({ category: 'UserActions', action: 'Clicking On Side By Side View' });
  };

  render() {
    const { locationMatches, locationId, isBaseRecord, orgId } = this.props;
    return isBaseRecord ? (
      <div>
        <div>
          <h4 className="title">
            <div className={'collapseBtn'} onClick={this.toggleAreaOpen}>
              <div className="collapseIcon">
                <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
              </div>
              <Translate contentKey={'serviceNetApp.service.matches'} />
            </div>
          </h4>
          <Collapse isOpen={this.state.isAreaOpen}>
            {locationMatches &&
              _.map(locationMatches[locationId], (field, i) => (
                <div key={i}>
                  <Link onClick={this.handleMatchClick(field.orgId)} to={`/multi-record-view/${orgId}/${field.orgId}`}>
                    {`${field.organizationName} - ${field.locationName}`}
                  </Link>
                </div>
              ))}
          </Collapse>
        </div>
      </div>
    ) : (
      ''
    );
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = { getPartnerRecord };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(LocationMatchesDetails);
