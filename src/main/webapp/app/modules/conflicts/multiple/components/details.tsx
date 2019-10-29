import React from 'react';
import '../multiple-record-view.scss';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { OrganizationDetails } from '../../shared/components/organization-details';
import { LocationsDetails } from '../../shared/components/location/locations-details';
import { ServicesDetails } from '../../shared/components/service/services-details';
import { ContactsDetails } from '../../shared/components/contact/contacts-details';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate } from 'react-jhipster';

export interface IMultipleRecordViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {
  activity: IActivityRecord;
  exclusions: any[];
  isBaseRecord: boolean;
  showClipboard: boolean;
  selectLocation?: any;
  matchingLocation?: any;
  matchLocations?: boolean;
  toggleMatchLocations?: any;
}

export interface IMultiRecordViewState {
  isAreaOpen: boolean;
}

export class Details extends React.Component<IMultipleRecordViewProp, IMultiRecordViewState> {
  state: IMultiRecordViewState = {
    isAreaOpen: false
  };

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  render() {
    const { isAreaOpen } = this.state;
    const columnSize = 12;
    return (
      <div>
        <OrganizationDetails {...this.props} sideSection={null} columnSize={columnSize} />
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
