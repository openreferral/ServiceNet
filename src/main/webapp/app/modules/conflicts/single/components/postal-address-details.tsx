import React from 'react';
import '../single-record-view.scss';
import { connect } from 'react-redux';
import InputField from './input-field';
import { IActivity } from 'app/shared/model/activity.model';
import { IPostalAddress } from 'app/shared/model/postal-address.model';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate } from 'react-jhipster';
import { Collapse } from 'reactstrap';

export interface IPostalAddressDetailsProp extends StateProps, DispatchProps {
  activity: IActivity;
  address: IPostalAddress;
}

export interface IPostalAddressDetailsState {
  isAreaOpen: boolean;
}

export class PostalAddressDetails extends React.Component<IPostalAddressDetailsProp, IPostalAddressDetailsState> {
  state: IPostalAddressDetailsState = {
    isAreaOpen: true
  };

  toggleLocationAreaOpen = () => {
    this.setState({
      isAreaOpen: !this.state.isAreaOpen
    });
  };

  render() {
    const { address } = this.props;

    return (
      <div>
        <h4 className="title">
          <div className="collapseBtn" onClick={this.toggleLocationAreaOpen}>
            <div className="collapseIcon">
              <FontAwesomeIcon size="xs" icon={this.state.isAreaOpen ? 'angle-up' : 'angle-down'} />
            </div>
            <Translate contentKey="singleRecordView.details.physicalAddressTitle" />
          </div>
        </h4>
        <Collapse isOpen={this.state.isAreaOpen}>
          <InputField {...this.props} entityClass="PostalAddress" type="text" fieldName="attention" defaultValue={address.attention} />
          <InputField {...this.props} entityClass="PostalAddress" type="text" fieldName="address1" defaultValue={address.address1} />
          <InputField {...this.props} entityClass="PostalAddress" type="text" fieldName="city" defaultValue={address.city} />
          <InputField {...this.props} entityClass="PostalAddress" type="text" fieldName="region" defaultValue={address.region} />
          <InputField
            {...this.props}
            entityClass="PostalAddress"
            type="text"
            fieldName="stateProvince"
            defaultValue={address.stateProvince}
          />
          <InputField
            {...this.props}
            entityClass="PostalAddress"
            type="textarea"
            fieldName="postalCode"
            defaultValue={address.postalCode}
          />
          <InputField {...this.props} entityClass="PostalAddress" type="text" fieldName="country" defaultValue={address.country} />
        </Collapse>
      </div>
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
