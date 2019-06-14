import React from 'react';
import '../multiple-record-view.scss';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { OrganizationDetails } from '../../shared/components/organization-details';
import { LocationsDetails } from '../../shared/components/location/locations-details';
import { ServicesDetails } from '../../shared/components/service/services-details';
import { ContactsDetails } from '../../shared/components/contact/contacts-details';
import { IActivityRecord } from 'app/shared/model/activity-record.model';

export interface IMultipleRecordViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {
  activity: IActivityRecord;
  exclusions: any[];
  isBaseRecord: boolean;
  showClipboard: boolean;
}

export class Details extends React.Component<IMultipleRecordViewProp> {
  render() {
    const columnSize = 12;
    return (
      <div>
        <OrganizationDetails {...this.props} sideSection={null} columnSize={columnSize} />
        <LocationsDetails {...this.props} locations={this.props.activity.locations} columnSize={columnSize} />
        <ServicesDetails {...this.props} services={this.props.activity.services} columnSize={columnSize} />
        <ContactsDetails {...this.props} contacts={this.props.activity.contacts} columnSize={columnSize} />
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
