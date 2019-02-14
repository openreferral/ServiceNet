import React from 'react';
import { Col, Row, Button } from 'reactstrap';
import '../../single-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { PhysicalAddressDetails } from './physical-address-details';
import { PostalAddressDetails } from './postal-address-details';
import { OpeningHoursDetails } from '../opening-hours-details';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { AdditionalDetails } from '../additional-details';
import { ILocationRecord } from 'app/shared/model/location-record.model';
import { LanguagesDetails } from '../languages-details';
import { HolidayScheduleDetails } from '../holiday-schedule-details';

export interface ISingleLocationDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  record: ILocationRecord;
  locationsCount: string;
  changeRecord: any;
  isOnlyOne: boolean;
}

export interface ISingleLocationDetailsState {
  isAreaOpen: boolean;
}

export class SingleLocationDetails extends React.Component<ISingleLocationDetailsProp, ISingleLocationDetailsState> {
  state: ISingleLocationDetailsState = {
    isAreaOpen: false
  };

  toggleAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  getTextField = (record, fieldName) => ({
    type: 'text',
    fieldName,
    defaultValue: record.location[fieldName]
  });

  render() {
    const { record, isOnlyOne } = this.props;
    const customHeader = (
      <h4 className="title">
        <div className="collapseBtn" onClick={this.toggleAreaOpen}>
          <div className="collapseIcon">
            <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
          </div>
          <Translate contentKey="singleRecordView.details.titleLocations" /> {this.props.locationsCount}
        </div>
        {isOnlyOne ? null : (
          <Button className="primary" onClick={this.props.changeRecord}>
            <Translate contentKey="singleRecordView.details.seeAnotherRecord" />
          </Button>
        )}
      </h4>
    );
    const additionalFields = [
      <PhysicalAddressDetails key="physical-address-details" {...this.props} address={record.physicalAddress} />,
      <PostalAddressDetails key="postal-address-details" {...this.props} address={record.postalAddress} />,
      <OpeningHoursDetails key="opening-hours-details" {...this.props} hours={record.regularScheduleOpeningHours} />,
      <LanguagesDetails key="languages-details" {...this.props} langs={record.langs} />,
      <HolidayScheduleDetails key="holiday-schedule-details" {...this.props} schedule={record.holidaySchedule} />
    ];

    const fields = [
      this.getTextField(record, 'name'),
      this.getTextField(record, 'alternateName'),
      {
        type: 'textarea',
        fieldName: 'description',
        defaultValue: record.location.description
      },
      this.getTextField(record, 'transportation'),
      this.getTextField(record, 'latitude'),
      this.getTextField(record, 'longitude')
    ];
    return (
      <Row>
        <Col sm="6">
          <hr />
          <AdditionalDetails
            {...this.props}
            fields={fields}
            entityClass={'Location'}
            customHeader={customHeader}
            additionalFields={additionalFields}
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
)(SingleLocationDetails);
