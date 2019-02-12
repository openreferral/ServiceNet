import React from 'react';
import { Col, Row, Form, Button, Collapse } from 'reactstrap';
import '../single-record-view.scss';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import InputField from './input-field';
import { IActivity } from 'app/shared/model/activity.model';
import { ILocation } from 'app/shared/model/location.model';
import { PhysicalAddressDetails } from './physical-address-details';
import { PostalAddressDetails } from './postal-address-details';
import { OpeningHoursDetails } from './opening-hours-details';
import { IPhysicalAddress } from 'app/shared/model/physical-address.model';
import { IPostalAddress } from 'app/shared/model/postal-address.model';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IOpeningHours } from 'app/shared/model/opening-hours.model';

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

  render() {
    const { location, physicalAddress, postalAddress, isOnlyOne, hours } = this.props;

    return (
      <Row>
        <Col sm="6">
          <hr />
          <h4 className="title">
            <div className="collapseBtn" onClick={this.toggleAreaOpen}>
              <div className="collapseIcon">
                <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
              </div>
              <Translate contentKey="singleRecordView.details.locationsTitle" /> {this.props.locationsCount}
            </div>
            {isOnlyOne ? null : (
              <Button className="primary" onClick={this.props.changeRecord}>
                <Translate contentKey="singleRecordView.details.seeAnotherRecord" />
              </Button>
            )}
          </h4>
          <Collapse isOpen={this.state.isAreaOpen}>
            <Form>
              <InputField {...this.props} entityClass="Location" type="text" fieldName="name" defaultValue={location.name} />
              <InputField
                {...this.props}
                entityClass="Location"
                type="text"
                fieldName="alternateName"
                defaultValue={location.alternateName}
              />
              <InputField
                {...this.props}
                entityClass="Location"
                type="textarea"
                fieldName="description"
                defaultValue={location.description}
              />
              <InputField
                {...this.props}
                entityClass="Location"
                type="text"
                fieldName="transportation"
                defaultValue={location.transportation}
              />
              <InputField {...this.props} entityClass="Location" type="text" fieldName="latitude" defaultValue={location.latitude} />
              <InputField {...this.props} entityClass="Location" type="text" fieldName="longitude" defaultValue={location.longitude} />

              <PhysicalAddressDetails {...this.props} address={physicalAddress} />
              <PostalAddressDetails {...this.props} address={postalAddress} />
              <OpeningHoursDetails {...this.props} hours={hours} />
            </Form>
          </Collapse>
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
