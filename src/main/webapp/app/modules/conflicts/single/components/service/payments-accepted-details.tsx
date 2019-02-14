import React from 'react';
import '../../single-record-view.scss';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { AdditionalDetails } from '../additional-details';
import { IPaymentAccepted } from 'app/shared/model/payment-accepted.model';

export interface IPaymentsAcceptedDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  payments: IPaymentAccepted[];
}

export class PaymentsAcceptedDetails extends React.Component<IPaymentsAcceptedDetailsProp> {
  getTextField = (payment, fieldName) => ({
    type: 'text',
    fieldName,
    defaultValue: payment[fieldName]
  });

  render() {
    const { payments } = this.props;
    const fields = payments.map(payment => this.getTextField(payment, 'payment'));

    return fields.length > 0 ? (
      <AdditionalDetails
        {...this.props}
        fields={fields}
        entityClass={'PaymentAccepted'}
        customHeader={false}
        additionalFields={false}
        toggleAvailable
        isCustomToggle={false}
        customToggleValue={false}
      />
    ) : null;
  }
}

const mapStateToProps = () => ({});

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PaymentsAcceptedDetails);
