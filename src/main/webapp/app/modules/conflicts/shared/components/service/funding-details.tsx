import React from 'react';
import '../../shared-record-view.scss';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { AdditionalDetails } from '../additional-details';
import { IFunding } from 'app/shared/model/funding.model';
import { getTextField } from 'app/shared/util/single-record-view-utils';

export interface IFundingDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  funding: IFunding;
  showClipboard: boolean;
}

export class FundingDetails extends React.Component<IFundingDetailsProp> {
  render() {
    const funding = this.props.funding ? this.props.funding : {};
    const fields = [getTextField(funding, 'source')];

    return (
      <AdditionalDetails
        {...this.props}
        fields={fields}
        entityClass={'Funding'}
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
)(FundingDetails);
