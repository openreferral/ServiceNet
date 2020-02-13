import React from 'react';
import { connect } from 'react-redux';
import { Button, Col, Row, Container, Input } from 'reactstrap';
import { Translate } from 'react-jhipster';
import Select from 'react-select';
import _ from 'lodash';

import { IRootState } from 'app/shared/reducers';
import { getUserEntities, createEntity, deleteEntity, reset } from 'app/entities/fields-display-settings/fields-display-settings.reducer';
import { getEntities as getLocationFieldsValues } from 'app/entities/location-fields-value/location-fields-value.reducer';
import { getEntities as getOrganizationFieldsValues } from 'app/entities/organization-fields-value/organization-fields-value.reducer';
import { getEntities as getPhysicalAddressFieldsValues } from 'app/entities/physical-address-fields-value/physical-address-fields-value.reducer';
import { getEntities as getPostalAddressFieldsValues } from 'app/entities/postal-address-fields-value/postal-address-fields-value.reducer';
import { getEntities as getServiceFieldsValues } from 'app/entities/service-fields-value/service-fields-value.reducer';
import { getEntities as getServiceTaxonomiesDetailsFieldsValues } from 'app/entities/service-taxonomies-details-fields-value/service-taxonomies-details-fields-value.reducer';
import { getEntities as getContactDetailsFieldsValues } from 'app/entities/contact-details-fields-value/contact-details-fields-value.reducer';
import FieldsDisplaySettingsDeleteConfirmation from './fields-display-settings-delete-confirmation';

export interface IFieldsDisplaySettingsPanelState {
  selectedSetting: any;
  settingName: string;
  deleteDialogOpen: boolean;
}

const INITIAL_SELECTED_SETTING = {
  id: null,
  name: '',
  value: null,
  label: '',
  locationFields: [],
  organizationFields: [],
  physicalAddressFields: [],
  postalAddressFields: [],
  serviceFields: [],
  serviceTaxonomiesDetailsFields: [],
  contactDetailsFields: [],
  userId: null,
  userLogin: ''
};

export interface IFieldsDisplaySettingsPanelProps extends StateProps, DispatchProps {}

export class FieldsDisplaySettingsPanel extends React.Component<IFieldsDisplaySettingsPanelProps, IFieldsDisplaySettingsPanelState> {
  state: IFieldsDisplaySettingsPanelState = {
    selectedSetting: INITIAL_SELECTED_SETTING,
    settingName: '',
    deleteDialogOpen: false
  };

  componentDidMount() {
    if (!this.props.isLoggingOut) {
      this.props.getUserEntities();
      this.props.getLocationFieldsValues();
      this.props.getOrganizationFieldsValues();
      this.props.getPhysicalAddressFieldsValues();
      this.props.getPostalAddressFieldsValues();
      this.props.getServiceFieldsValues();
      this.props.getServiceTaxonomiesDetailsFieldsValues();
      this.props.getContactDetailsFieldsValues();
    }
  }

  saveSettings = () => {
    const settings = { ...this.state.selectedSetting, userId: this.props.userId, name: this.state.settingName, id: null };
    this.props.createEntity(settings);
  };

  deleteSetting = () => {
    this.setState({
      deleteDialogOpen: true
    });
  };

  handleDeleteDialogClose = () => {
    this.setState({
      deleteDialogOpen: false
    });
  };

  handleConfirmDelete = id => {
    this.props.reset();
    this.props.deleteEntity(id);
    this.setState({ selectedSetting: INITIAL_SELECTED_SETTING });
  };

  handleSettingChange = selectedSetting => {
    this.setState({ selectedSetting, settingName: '' });
  };

  handleSettingNameChange = event => {
    this.setState({ settingName: event.target.value });
  };

  handleDeleteDialogConfirm = id => {
    this.props.deleteEntity(id);
    this.setState({
      deleteDialogOpen: false,
      selectedSetting: INITIAL_SELECTED_SETTING
    });
  };

  selectAll = () => {
    this.setState({
      selectedSetting: {
        ...this.state.selectedSetting,
        organizationFields: this.props.organizationFieldsOptions.map(l => l.label),
        locationFields: this.props.locationFieldsOptions.map(l => l.label),
        physicalAddressFields: this.props.physicalAddressFieldsOptions.map(l => l.label),
        postalAddressFields: this.props.postalAddressFieldsOptions.map(l => l.label),
        serviceFields: this.props.serviceFieldsOptions.map(l => l.label),
        serviceTaxonomiesDetailsFields: this.props.serviceTaxonomiesDetailsFieldsOptions.map(l => l.label),
        contactDetailsFields: this.props.contactDetailsFieldsOptions.map(l => l.label)
      }
    });
  };

  updateSelectedSettings = setting => {
    this.setState({ selectedSetting: { ...setting } });
  };

  handleOrganizationFieldsChange = organizationFields => {
    const organizationFieldsMapped = organizationFields.map(l => l.label);
    this.updateSelectedSettings({ ...this.state.selectedSetting, organizationFields: organizationFieldsMapped });
  };

  handleLocationFieldsChange = locationFields => {
    const locationFieldsMapped = locationFields.map(l => l.label);
    let { physicalAddressFields, postalAddressFields } = this.state.selectedSetting;
    if (locationFieldsMapped.indexOf('PHYSICAL_ADDRESS') < 0) {
      physicalAddressFields = [];
    }
    if (locationFieldsMapped.indexOf('POSTAL_ADDRESS') < 0) {
      postalAddressFields = [];
    }
    this.updateSelectedSettings({
      ...this.state.selectedSetting,
      locationFields: locationFieldsMapped,
      physicalAddressFields,
      postalAddressFields
    });
  };

  handlePhysicalAddressFieldsChange = physicalAddressFields => {
    const physicalAddressFieldsMapped = physicalAddressFields.map(l => l.label);
    const { locationFields } = this.state.selectedSetting;
    if (this.state.selectedSetting.locationFields.indexOf('PHYSICAL_ADDRESS') < 0 && physicalAddressFieldsMapped.length > 0) {
      locationFields.push('PHYSICAL_ADDRESS');
    }
    this.updateSelectedSettings({ ...this.state.selectedSetting, physicalAddressFields: physicalAddressFieldsMapped, locationFields });
  };

  handlePostalAddressFieldsChange = postalAddressFields => {
    const postalAddressFieldsMapped = postalAddressFields.map(l => l.label);
    const { locationFields } = this.state.selectedSetting;
    if (this.state.selectedSetting.locationFields.indexOf('POSTAL_ADDRESS') < 0 && postalAddressFields.length > 0) {
      locationFields.push('POSTAL_ADDRESS');
    }
    this.updateSelectedSettings({ ...this.state.selectedSetting, postalAddressFields: postalAddressFieldsMapped, locationFields });
  };

  handleServiceFieldsChange = serviceFields => {
    const serviceFieldsMapped = serviceFields.map(l => l.label);
    let { serviceTaxonomiesDetailsFields, contactDetailsFields } = this.state.selectedSetting;
    if (serviceFieldsMapped.indexOf('TAXONOMIES') < 0) {
      serviceTaxonomiesDetailsFields = [];
    }
    if (serviceFieldsMapped.indexOf('CONTACTS') < 0) {
      contactDetailsFields = [];
    }
    this.updateSelectedSettings({
      ...this.state.selectedSetting,
      serviceFields: serviceFieldsMapped,
      serviceTaxonomiesDetailsFields,
      contactDetailsFields
    });
  };

  handleServiceTaxonomiesDetailsFieldsChange = serviceTaxonomiesDetailsFields => {
    const serviceTaxonomiesDetailsFieldsMapped = serviceTaxonomiesDetailsFields.map(l => l.label);
    const { serviceFields } = this.state.selectedSetting;
    if (this.state.selectedSetting.serviceFields.indexOf('TAXONOMIES') < 0 && serviceTaxonomiesDetailsFieldsMapped.length > 0) {
      serviceFields.push('TAXONOMIES');
    }
    this.updateSelectedSettings({
      ...this.state.selectedSetting,
      serviceTaxonomiesDetailsFields: serviceTaxonomiesDetailsFieldsMapped,
      serviceFields
    });
  };

  handleContactDetailsFieldsChange = contactDetailsFields => {
    const contactDetailsFieldsMapped = contactDetailsFields.map(l => l.label);
    const { serviceFields } = this.state.selectedSetting;
    if (this.state.selectedSetting.serviceFields.indexOf('CONTACTS') < 0 && contactDetailsFieldsMapped.length > 0) {
      serviceFields.push('CONTACTS');
    }
    this.updateSelectedSettings({ ...this.state.selectedSetting, contactDetailsFields: contactDetailsFieldsMapped, serviceFields });
  };

  render() {
    return (
      <div>
        <Container>
          <Row>
            <Col md="6">
              <Translate contentKey="serviceNetApp.fieldsDisplaySettings.savedSettings" />
              <Select
                value={this.state.selectedSetting}
                onChange={this.handleSettingChange}
                options={this.props.fieldsDisplaySettingsOptions}
              />
            </Col>
            <Col md="6">
              <Translate contentKey="serviceNetApp.fieldsDisplaySettings.settingName" />
              <Input type="text" className="form-control" value={this.state.settingName} onChange={this.handleSettingNameChange} />
            </Col>
          </Row>
          <Row>
            <Col md={{ size: 2 }}>
              <Button
                color="danger"
                onClick={this.deleteSetting}
                disabled={!this.state.selectedSetting.id}
                style={{ marginTop: '1rem' }}
                block
              >
                <Translate contentKey="serviceNetApp.fieldsDisplaySettings.deleteSettings" />
                {this.state.deleteDialogOpen && (
                  <FieldsDisplaySettingsDeleteConfirmation
                    selectedSetting={this.state.selectedSetting}
                    handleClose={this.handleDeleteDialogClose}
                    handleConfirmDelete={this.handleConfirmDelete}
                  />
                )}
              </Button>
            </Col>
            <Col md={{ size: 2, offset: 6 }}>
              <Button color="secondary" onClick={this.selectAll} style={{ marginTop: '1rem' }} block>
                <Translate contentKey={`serviceNetApp.fieldsDisplaySettings.selectAll`} />
              </Button>
            </Col>
            <Col md={{ size: 2 }}>
              <Button color="primary" onClick={this.saveSettings} disabled={!this.state.settingName} style={{ marginTop: '1rem' }} block>
                <Translate contentKey="serviceNetApp.fieldsDisplaySettings.saveSettings" />
              </Button>
            </Col>
          </Row>
          <Row className="my-1">
            <Col>
              <Translate contentKey={`serviceNetApp.fieldsDisplaySettings.organizationFields`} />
              <Select
                isMulti
                onChange={this.handleOrganizationFieldsChange}
                options={this.props.organizationFieldsOptions}
                value={this.state.selectedSetting.organizationFields.map(f => ({
                  value: f,
                  label: f
                }))}
              />
            </Col>
          </Row>
          <Row className="my-1">
            <Col>
              <Translate contentKey={`serviceNetApp.fieldsDisplaySettings.locationFields`} />
              <Select
                isMulti
                onChange={this.handleLocationFieldsChange}
                options={this.props.locationFieldsOptions}
                value={this.state.selectedSetting.locationFields.map(f => ({
                  value: f,
                  label: f
                }))}
              />
            </Col>
          </Row>
          <Row className="my-1">
            <Col>
              <Translate contentKey={`serviceNetApp.fieldsDisplaySettings.physicalAddressFields`} />
              <Select
                isMulti
                onChange={this.handlePhysicalAddressFieldsChange}
                options={this.props.physicalAddressFieldsOptions}
                value={this.state.selectedSetting.physicalAddressFields.map(f => ({
                  value: f,
                  label: f
                }))}
              />
            </Col>
          </Row>
          <Row className="my-1">
            <Col>
              <Translate contentKey={`serviceNetApp.fieldsDisplaySettings.postalAddressFields`} />
              <Select
                isMulti
                onChange={this.handlePostalAddressFieldsChange}
                options={this.props.postalAddressFieldsOptions}
                value={this.state.selectedSetting.postalAddressFields.map(f => ({
                  value: f,
                  label: f
                }))}
              />
            </Col>
          </Row>
          <Row className="my-1">
            <Col>
              <Translate contentKey={`serviceNetApp.fieldsDisplaySettings.serviceFields`} />
              <Select
                isMulti
                onChange={this.handleServiceFieldsChange}
                options={this.props.serviceFieldsOptions}
                value={this.state.selectedSetting.serviceFields.map(f => ({
                  value: f,
                  label: f
                }))}
              />
            </Col>
          </Row>
          <Row className="my-1">
            <Col>
              <Translate contentKey={`serviceNetApp.fieldsDisplaySettings.serviceTaxonomiesDetailsFields`} />
              <Select
                isMulti
                onChange={this.handleServiceTaxonomiesDetailsFieldsChange}
                options={this.props.serviceTaxonomiesDetailsFieldsOptions}
                value={this.state.selectedSetting.serviceTaxonomiesDetailsFields.map(f => ({
                  value: f,
                  label: f
                }))}
              />
            </Col>
          </Row>
          <Row className="my-1">
            <Col>
              <Translate contentKey={`serviceNetApp.fieldsDisplaySettings.contactDetailsFields`} />
              <Select
                isMulti
                onChange={this.handleContactDetailsFieldsChange}
                options={this.props.contactDetailsFieldsOptions}
                value={this.state.selectedSetting.contactDetailsFields.map(f => ({
                  value: f,
                  label: f
                }))}
              />
            </Col>
          </Row>
        </Container>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  fieldsDisplaySettingsOptions: _.union(
    [{ ...INITIAL_SELECTED_SETTING }],
    storeState.fieldsDisplaySettings.entities.map(o => ({ ...o, value: o.id, label: o.name }))
  ),
  fieldsDisplaySettingsEntity: storeState.fieldsDisplaySettings.entity,
  isLoggingOut: storeState.authentication.loggingOut,
  userId: storeState.authentication.account.id,
  // Each field setting options for selects
  organizationFieldsOptions: storeState.organizationFieldsValue.entities.map(o => ({
    value: o.organizationField,
    label: o.organizationField
  })),
  locationFieldsOptions: storeState.locationFieldsValue.entities.map(o => ({ value: o.locationField, label: o.locationField })),
  physicalAddressFieldsOptions: storeState.physicalAddressFieldsValue.entities.map(o => ({
    value: o.physicalAddressField,
    label: o.physicalAddressField
  })),
  postalAddressFieldsOptions: storeState.postalAddressFieldsValue.entities.map(o => ({
    value: o.postalAddressField,
    label: o.postalAddressField
  })),
  serviceFieldsOptions: storeState.serviceFieldsValue.entities.map(o => ({ value: o.serviceField, label: o.serviceField })),
  serviceTaxonomiesDetailsFieldsOptions: storeState.serviceTaxonomiesDetailsFieldsValue.entities.map(o => ({
    value: o.serviceTaxonomiesDetailsField,
    label: o.serviceTaxonomiesDetailsField
  })),
  contactDetailsFieldsOptions: storeState.contactDetailsFieldsValue.entities.map(o => ({
    value: o.contactDetailsField,
    label: o.contactDetailsField
  }))
});

const mapDispatchToProps = {
  getUserEntities,
  createEntity,
  deleteEntity,
  reset,
  getLocationFieldsValues,
  getOrganizationFieldsValues,
  getPhysicalAddressFieldsValues,
  getPostalAddressFieldsValues,
  getServiceFieldsValues,
  getServiceTaxonomiesDetailsFieldsValues,
  getContactDetailsFieldsValues
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FieldsDisplaySettingsPanel);
