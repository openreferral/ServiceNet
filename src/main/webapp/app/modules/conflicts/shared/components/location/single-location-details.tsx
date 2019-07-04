import React from 'react';
import { Col, Row, Button } from 'reactstrap';
import '../../shared-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { IActivityRecord } from 'app/shared/model/activity-record.model';
import { PhysicalAddressDetails } from './physical-address-details';
import { PostalAddressDetails } from './postal-address-details';
import { OpeningHoursDetails } from '../opening-hours-details';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { AdditionalDetails } from '../additional-details';
import { ILocationRecord } from 'app/shared/model/location-record.model';
import { LanguagesDetails } from '../languages-details';
import { HolidayScheduleDetails } from '../holiday-schedule-details';
import { getTextField } from 'app/shared/util/single-record-view-utils';

export interface ISingleLocationDetailsProp extends StateProps, DispatchProps {
  activity: IActivityRecord;
  record: ILocationRecord;
  locationsCount: string;
  changeRecord: any;
  isOnlyOne: boolean;
  columnSize: number;
  showClipboard: boolean;
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

  changeRecord = offset => () => {
    this.setState({ isAreaOpen: true });
    this.props.changeRecord(offset);
  };

  render() {
    const { record, isOnlyOne, columnSize } = this.props;
    const customHeader = (
      <h4 className="title">
        <div className="collapseBtn" onClick={this.toggleAreaOpen}>
          <div className="collapseIcon">
            <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
          </div>
          <Translate contentKey="singleRecordView.details.titleLocations" /> <span className="text-blue">{this.props.locationsCount}</span>
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
    const additionalFields = [
      <PhysicalAddressDetails key="physical-address-details" {...this.props} address={record.physicalAddress} />,
      <PostalAddressDetails key="postal-address-details" {...this.props} address={record.postalAddress} />,
      <OpeningHoursDetails key="opening-hours-details" {...this.props} hours={record.regularScheduleOpeningHours} />,
      <LanguagesDetails key="languages-details" {...this.props} langs={record.langs} />,
      <HolidayScheduleDetails key="holiday-schedule-details" {...this.props} schedule={record.holidaySchedule} />
    ];

    const fields = [
      getTextField(record.location, 'name'),
      getTextField(record.location, 'alternateName'),
      {
        type: 'textarea',
        fieldName: 'description',
        defaultValue: record.location.description
      },
      getTextField(record.location, 'transportation'),
      getTextField(record.location, 'latitude'),
      getTextField(record.location, 'longitude')
    ];
    return (
      <Row>
        <Col sm={columnSize}>
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
