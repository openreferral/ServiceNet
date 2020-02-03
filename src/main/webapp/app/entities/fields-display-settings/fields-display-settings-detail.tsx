import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './fields-display-settings.reducer';
import { IFieldsDisplaySettings } from 'app/shared/model/fields-display-settings.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFieldsDisplaySettingsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FieldsDisplaySettingsDetail extends React.Component<IFieldsDisplaySettingsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { fieldsDisplaySettingsEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.fieldsDisplaySettings.detail.title">FieldsDisplaySettings</Translate> [
            <b>{fieldsDisplaySettingsEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="serviceNetApp.fieldsDisplaySettings.name">Name</Translate>
              </span>
            </dt>
            <dd>{fieldsDisplaySettingsEntity.name}</dd>
            <dt>
              <span id="locationFields">
                <Translate contentKey="serviceNetApp.fieldsDisplaySettings.locationFields">Location Fields</Translate>
              </span>
            </dt>
            <dd>{fieldsDisplaySettingsEntity.locationFields}</dd>
            <dt>
              <span id="organizationFields">
                <Translate contentKey="serviceNetApp.fieldsDisplaySettings.organizationFields">Organization Fields</Translate>
              </span>
            </dt>
            <dd>{fieldsDisplaySettingsEntity.organizationFields}</dd>
            <dt>
              <span id="physicalAddressFields">
                <Translate contentKey="serviceNetApp.fieldsDisplaySettings.physicalAddressFields">Physical Address Fields</Translate>
              </span>
            </dt>
            <dd>{fieldsDisplaySettingsEntity.physicalAddressFields}</dd>
            <dt>
              <span id="postalAddressFields">
                <Translate contentKey="serviceNetApp.fieldsDisplaySettings.postalAddressFields">Postal Address Fields</Translate>
              </span>
            </dt>
            <dd>{fieldsDisplaySettingsEntity.postalAddressFields}</dd>
            <dt>
              <span id="serviceFields">
                <Translate contentKey="serviceNetApp.fieldsDisplaySettings.serviceFields">Service Fields</Translate>
              </span>
            </dt>
            <dd>{fieldsDisplaySettingsEntity.serviceFields}</dd>
            <dt>
              <span id="serviceTaxonomiesDetailsFields">
                <Translate contentKey="serviceNetApp.fieldsDisplaySettings.serviceTaxonomiesDetailsFields">
                  Service Taxonomies Details Fields
                </Translate>
              </span>
            </dt>
            <dd>{fieldsDisplaySettingsEntity.serviceTaxonomiesDetailsFields}</dd>
            <dt>
              <span id="contactDetailsFields">
                <Translate contentKey="serviceNetApp.fieldsDisplaySettings.contactDetailsFields">Contact Details Fields</Translate>
              </span>
            </dt>
            <dd>{fieldsDisplaySettingsEntity.contactDetailsFields}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.fieldsDisplaySettings.user">User</Translate>
            </dt>
            <dd>{fieldsDisplaySettingsEntity.userLogin ? fieldsDisplaySettingsEntity.userLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/fields-display-settings" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/fields-display-settings/${fieldsDisplaySettingsEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ fieldsDisplaySettings }: IRootState) => ({
  fieldsDisplaySettingsEntity: fieldsDisplaySettings.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FieldsDisplaySettingsDetail);
