import React from 'react';
import { Col, Row, Button } from 'reactstrap';
import '../single-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { IActivity } from 'app/shared/model/activity.model';
import { ILocation } from 'app/shared/model/location.model';
import { PhysicalAddressDetails } from './physical-address-details';
import { PostalAddressDetails } from './postal-address-details';
import { OpeningHoursDetails } from './opening-hours-details';
import { IPhysicalAddress } from 'app/shared/model/physical-address.model';
import { IPostalAddress } from 'app/shared/model/postal-address.model';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IOpeningHours } from 'app/shared/model/opening-hours.model';
import { AdditionalDetails } from './additional-details';

export interface ISingleLocationDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  location: ILocation;
  physicalAddress: IPhysicalAddress;
  postalAddress: IPostalAddress;
  locationsCount: string;
  changeRecord: any;
  isOnlyOne: boolean;
  hours: IOpeningHours[];
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

  getTextField = (location, fieldName) => {
    return {
      type: 'text',
      fieldName: fieldName,
      defaultValue: location[fieldName]
    };
  };

  render() {
    const { location, physicalAddress, postalAddress, isOnlyOne, hours } = this.props;
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
      <PhysicalAddressDetails {...this.props} address={physicalAddress} />,
      <PostalAddressDetails {...this.props} address={postalAddress} />,
      <OpeningHoursDetails {...this.props} hours={hours} />
    ];

    const fields = [
      this.getTextField(location, 'name'),
      this.getTextField(location, 'alternateName'),
      {
        type: 'textarea',
        fieldName: 'description',
        defaultValue: location.description
      },
      this.getTextField(location, 'transportation'),
      this.getTextField(location, 'latitude'),
      this.getTextField(location, 'longitude')
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
            toggleAvailable={true}
            isCustomToggle={true}
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
