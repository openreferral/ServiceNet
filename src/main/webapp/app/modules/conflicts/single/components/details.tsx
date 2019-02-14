import React from 'react';
import '../single-record-view.scss';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { IActivity } from 'app/shared/model/activity.model';
import { OrganizationDetails } from './organization-details';
import { LocationsDetails } from './location/locations-details';

export interface ISingleRecordViewProp extends StateProps, DispatchProps, RouteComponentProps<{}> {
  activity: IActivity;
}

export interface ISingleRecordViewState {
  activeTab: string;
}

export class Details extends React.Component<ISingleRecordViewProp, ISingleRecordViewState> {
  state: ISingleRecordViewState = {
    activeTab: '1'
  };

  toggle = tab => {
    if (this.state.activeTab !== tab) {
      this.setState({
        activeTab: tab
      });
    }
  };

  render() {
    return (
      <div>
        <OrganizationDetails {...this.props} />
        <LocationsDetails {...this.props} />
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
