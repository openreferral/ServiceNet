import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { IPhone } from 'app/shared/model/phone.model';
import { SinglePhoneDetails } from '../phone/single-phone-details';

export interface IPhonesDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  phones: IPhone[];
  columnSize: number;
  showClipboard: boolean;
}

export interface IPhonesDetailsState {
  phonesNumber: number;
}

export class PhonesDetails extends React.Component<IPhonesDetailsProp, IPhonesDetailsState> {
  state: IPhonesDetailsState = {
    phonesNumber: 0
  };

  changeRecord = () => {
    let phonesNumber = 0;
    if (this.state.phonesNumber !== this.props.phones.length - 1) {
      phonesNumber = this.state.phonesNumber + 1;
    }
    this.setState({ phonesNumber });
  };

  render() {
    const { phones } = this.props;
    const { phonesNumber } = this.state;
    const phone = phones[phonesNumber];
    const phoneDetails =
      phones.length > phonesNumber ? (
        <SinglePhoneDetails
          {...this.props}
          changeRecord={this.changeRecord}
          isOnlyOne={phones.length <= 1}
          phone={phone}
          phonesCount={`(${phonesNumber + 1}/${phones.length}) `}
        />
      ) : null;

    return phoneDetails;
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PhonesDetails);
