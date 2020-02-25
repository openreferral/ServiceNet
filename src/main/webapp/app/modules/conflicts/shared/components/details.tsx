import React from 'react';
import '../../all/all-records-view.scss';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { OrganizationDetails } from '../../shared/components/organization-details';
import LocationsDetails from '../../shared/components/location/locations-details';
import ServicesDetails from '../../shared/components/service/services-details';
import { ContactsDetails } from '../../shared/components/contact/contacts-details';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate } from 'react-jhipster';

export interface IDetailsProp extends StateProps, DispatchProps, RouteComponentProps<{}> {
  activity: IActivityRecord;
  exclusions: any[];
  isBaseRecord: boolean;
  showClipboard: boolean;
  selectLocation?: any;
  matchingLocation?: any;
  matchLocations?: boolean;
  toggleMatchLocations?: any;
  settings?: any;
  serviceMatches?: any;
  locationMatches?: any;
}

export interface IDetailsState {
  isAreaOpen: boolean;
}

export class Details extends React.Component<IDetailsProp, IDetailsState> {
  state: IDetailsState = {
    isAreaOpen: false
  };

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  displaySection = name => {
    const { settings } = this.props;
    if (settings === undefined || (!!settings && !settings.id)) return true;
    return !!this.props.settings && this.props.settings[name] && this.props.settings[name].length > 0;
  };

  render() {
    const { isAreaOpen } = this.state;
    const { settings, serviceMatches, locationMatches } = this.props;
    const columnSize = 12;
    return (
      <div>
        {this.displaySection('organizationFields') ? (
          <OrganizationDetails {...this.props} sideSection={null} columnSize={columnSize} settings={settings} />
        ) : null}
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
            {this.displaySection('locationFields') ? (
              <LocationsDetails
                {...this.props}
                locations={this.props.activity.locations}
                columnSize={columnSize}
                isAreaOpen
                settings={settings}
                locationMatches={locationMatches}
              />
            ) : null}
            {this.displaySection('serviceFields') ? (
              <ServicesDetails
                {...this.props}
                services={this.props.activity.services}
                columnSize={columnSize}
                isAreaOpen
                settings={settings}
                serviceMatches={serviceMatches}
              />
            ) : null}
            {this.displaySection('contactDetailsFields') ? (
              <ContactsDetails
                {...this.props}
                contacts={this.props.activity.contacts}
                columnSize={columnSize}
                isAreaOpen
                settings={settings}
              />
            ) : null}
          </div>
        ) : null}
        {isAreaOpen ? null : (
          <div>
            {this.displaySection('locationFields') ? (
              <LocationsDetails
                {...this.props}
                locations={this.props.activity.locations}
                columnSize={columnSize}
                isAreaOpen={false}
                settings={settings}
                locationMatches={locationMatches}
              />
            ) : null}
            {this.displaySection('serviceFields') ? (
              <ServicesDetails
                {...this.props}
                services={this.props.activity.services}
                columnSize={columnSize}
                isAreaOpen={false}
                settings={settings}
                serviceMatches={serviceMatches}
              />
            ) : null}
            {this.displaySection('contactDetailsFields') ? (
              <ContactsDetails
                {...this.props}
                contacts={this.props.activity.contacts}
                columnSize={columnSize}
                isAreaOpen={false}
                settings={settings}
              />
            ) : null}
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
