import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './service-taxonomies-details-fields-value.reducer';
import { IServiceTaxonomiesDetailsFieldsValue } from 'app/shared/model/service-taxonomies-details-fields-value.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IServiceTaxonomiesDetailsFieldsValueUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IServiceTaxonomiesDetailsFieldsValueUpdateState {
  isNew: boolean;
}

export class ServiceTaxonomiesDetailsFieldsValueUpdate extends React.Component<
  IServiceTaxonomiesDetailsFieldsValueUpdateProps,
  IServiceTaxonomiesDetailsFieldsValueUpdateState
> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { serviceTaxonomiesDetailsFieldsValueEntity } = this.props;
      const entity = {
        ...serviceTaxonomiesDetailsFieldsValueEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/service-taxonomies-details-fields-value');
  };

  render() {
    const { serviceTaxonomiesDetailsFieldsValueEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="serviceNetApp.serviceTaxonomiesDetailsFieldsValue.home.createOrEditLabel">
              <Translate contentKey="serviceNetApp.serviceTaxonomiesDetailsFieldsValue.home.createOrEditLabel">
                Create or edit a ServiceTaxonomiesDetailsFieldsValue
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : serviceTaxonomiesDetailsFieldsValueEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="service-taxonomies-details-fields-value-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput
                      id="service-taxonomies-details-fields-value-id"
                      type="text"
                      className="form-control"
                      name="id"
                      required
                      readOnly
                    />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label
                    id="serviceTaxonomiesDetailsFieldLabel"
                    for="service-taxonomies-details-fields-value-serviceTaxonomiesDetailsField"
                  >
                    <Translate contentKey="serviceNetApp.serviceTaxonomiesDetailsFieldsValue.serviceTaxonomiesDetailsField">
                      Service Taxonomies Details Field
                    </Translate>
                  </Label>
                  <AvInput
                    id="service-taxonomies-details-fields-value-serviceTaxonomiesDetailsField"
                    type="select"
                    className="form-control"
                    name="serviceTaxonomiesDetailsField"
                    value={(!isNew && serviceTaxonomiesDetailsFieldsValueEntity.serviceTaxonomiesDetailsField) || 'TAXONOMY_NAME'}
                  >
                    <option value="TAXONOMY_NAME">{translate('serviceNetApp.ServiceTaxonomiesDetailsFields.TAXONOMY_NAME')}</option>
                    <option value="TAXONOMY_DETAILS">{translate('serviceNetApp.ServiceTaxonomiesDetailsFields.TAXONOMY_DETAILS')}</option>
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/service-taxonomies-details-fields-value" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  serviceTaxonomiesDetailsFieldsValueEntity: storeState.serviceTaxonomiesDetailsFieldsValue.entity,
  loading: storeState.serviceTaxonomiesDetailsFieldsValue.loading,
  updating: storeState.serviceTaxonomiesDetailsFieldsValue.updating,
  updateSuccess: storeState.serviceTaxonomiesDetailsFieldsValue.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServiceTaxonomiesDetailsFieldsValueUpdate);
