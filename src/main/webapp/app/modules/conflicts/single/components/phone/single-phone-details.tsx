import React from 'react';
import { Col, Row, Button } from 'reactstrap';
import '../../single-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { AdditionalDetails } from '../additional-details';
import { IPhone } from 'app/shared/model/phone.model';

export interface ISinglePhoneDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  phone: IPhone;
  phonesCount: string;
  changeRecord: any;
  isOnlyOne: boolean;
}

export interface ISinglePhoneDetailsState {
  isAreaOpen: boolean;
}

export class SinglePhoneDetails extends React.Component<ISinglePhoneDetailsProp, ISinglePhoneDetailsState> {
  state: ISinglePhoneDetailsState = {
    isAreaOpen: false
  };

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  getTextField = (phone, fieldName) => ({
    type: 'text',
    fieldName,
    defaultValue: phone[fieldName]
  });

  render() {
    const { phone, isOnlyOne } = this.props;
    const customHeader = (
      <h4 className="title">
        <div className="collapseBtn" onClick={this.toggleAreaOpen}>
          <div className="collapseIcon">
            <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
          </div>
          <Translate contentKey="singleRecordView.details.titlePhones" /> {this.props.phonesCount}
        </div>
        {isOnlyOne ? null : (
          <Button className="primary" onClick={this.props.changeRecord}>
            <Translate contentKey="singleRecordView.details.seeAnotherRecord" />
          </Button>
        )}
      </h4>
    );

    const fields = [
      this.getTextField(phone, 'number'),
      this.getTextField(phone, 'extension'),
      this.getTextField(phone, 'type'),
      this.getTextField(phone, 'language'),
      {
        type: 'textarea',
        fieldName: 'description',
        defaultValue: phone.description
      }
    ];
    return (
      <Row>
        <Col sm="6">
          <hr />
          <AdditionalDetails
            {...this.props}
            fields={fields}
            entityClass={'Phone'}
            customHeader={customHeader}
            additionalFields={false}
            toggleAvailable
            isCustomToggle
            customToggleValue={this.state.isAreaOpen}
          />
        </Col>
      </Row>
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
)(SinglePhoneDetails);
