import React from 'react';
import { Col, Row, Button } from 'reactstrap';
import '../../shared-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { AdditionalDetails } from '../additional-details';
import { IPhone } from 'app/shared/model/phone.model';
import { getTextField } from 'app/shared/util/single-record-view-utils';

export interface ISinglePhoneDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  phone: IPhone;
  phonesCount: string;
  changeRecord: any;
  isOnlyOne: boolean;
  columnSize: number;
  showClipboard: boolean;
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

  changeRecord = offset => () => {
    this.setState({ isAreaOpen: true });
    this.props.changeRecord(offset);
  };

  render() {
    const { phone, isOnlyOne, columnSize } = this.props;
    const customHeader = (
      <h4 className="title">
        <div className="collapseBtn" onClick={this.toggleAreaOpen}>
          <div className="collapseIcon">
            <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
          </div>
          <Translate contentKey="singleRecordView.details.titlePhones" /> <span className="text-blue">{this.props.phonesCount}</span>
        </div>
        {isOnlyOne ? null : (
          <span>
            <span role="button" onClick={this.changeRecord(-1)}>
              <FontAwesomeIcon className="text-blue" icon="chevron-left" /> <Translate contentKey="singleRecordView.details.prev" />
            </span>
            <span role="button" onClick={this.changeRecord(1)}>
              <Translate contentKey="singleRecordView.details.next" /> <FontAwesomeIcon className="text-blue" icon="chevron-right" />
            </span>
          </span>
        )}
      </h4>
    );

    const fields = [
      getTextField(phone, 'number'),
      getTextField(phone, 'extension'),
      getTextField(phone, 'type'),
      getTextField(phone, 'language'),
      {
        type: 'textarea',
        fieldName: 'description',
        defaultValue: phone.description
      }
    ];
    return (
      <Row>
        <Col sm={columnSize}>
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
