import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { IPostalAddress } from 'app/shared/model/postal-address.model';
import { AdditionalDetails } from '../additional-details';
import { getTextField } from 'app/shared/util/single-record-view-utils';
import _ from 'lodash';

export interface IPostalAddressDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  address: IPostalAddress;
  showClipboard: boolean;
  settings?: any;
}

export class PostalAddressDetails extends React.Component<IPostalAddressDetailsProp> {
  getFields = fieldsMap => {
    const { settings } = this.props;

    if (settings === undefined || (!!settings && !settings.id)) {
      return _.values(fieldsMap);
    }

    const { postalAddressFields } = settings;

    const fieldsMapKeys = _.keys(fieldsMap);
    const keysFiltered = _.filter(fieldsMapKeys, k => postalAddressFields.indexOf(k) > -1);
    return _.values(_.pick(fieldsMap, keysFiltered));
  };

  render() {
    const address = this.props.address ? this.props.address : {};
    const fields = {
      ATTENTION: getTextField(address, 'attention'),
      ADDRESS_1: getTextField(address, 'address1'),
      CITY: getTextField(address, 'city'),
      REGION: getTextField(address, 'region'),
      STATE_PROVINCE: getTextField(address, 'stateProvince'),
      POSTAL_CODE: getTextField(address, 'postalCode'),
      COUNTRY: getTextField(address, 'country')
    };

    return (
      <AdditionalDetails
        {...this.props}
        fields={this.getFields(fields)}
        entityClass={'PostalAddress'}
        customHeader={false}
        additionalFields={false}
        toggleAvailable
        isCustomToggle={false}
        customToggleValue={false}
      />
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
)(PostalAddressDetails);
