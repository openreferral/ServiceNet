import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './field-exclusion.reducer';
import { IFieldExclusion } from 'app/shared/model/field-exclusion.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFieldExclusionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FieldExclusionDetail extends React.Component<IFieldExclusionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { fieldExclusionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.fieldExclusion.detail.title">FieldExclusion</Translate> [<b>{fieldExclusionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="fields">
                <Translate contentKey="serviceNetApp.fieldExclusion.fields">Fields</Translate>
              </span>
            </dt>
            <dd>{fieldExclusionEntity.fields}</dd>
            <dt>
              <span id="entity">
                <Translate contentKey="serviceNetApp.fieldExclusion.entity">Entity</Translate>
              </span>
            </dt>
            <dd>{fieldExclusionEntity.entity}</dd>
          </dl>
          <Button tag={Link} to="/entity/field-exclusion" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/field-exclusion/${fieldExclusionEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ fieldExclusion }: IRootState) => ({
  fieldExclusionEntity: fieldExclusion.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FieldExclusionDetail);
