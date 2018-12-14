import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './metadata.reducer';
import { IMetadata } from 'app/shared/model/metadata.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMetadataDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MetadataDetail extends React.Component<IMetadataDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { metadataEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.metadata.detail.title">Metadata</Translate> [<b>{metadataEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="resourceId">
                <Translate contentKey="serviceNetApp.metadata.resourceId">Resource Id</Translate>
              </span>
            </dt>
            <dd>{metadataEntity.resourceId}</dd>
            <dt>
              <span id="lastActionDate">
                <Translate contentKey="serviceNetApp.metadata.lastActionDate">Last Action Date</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={metadataEntity.lastActionDate} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="lastActionType">
                <Translate contentKey="serviceNetApp.metadata.lastActionType">Last Action Type</Translate>
              </span>
            </dt>
            <dd>{metadataEntity.lastActionType}</dd>
            <dt>
              <span id="fieldName">
                <Translate contentKey="serviceNetApp.metadata.fieldName">Field Name</Translate>
              </span>
            </dt>
            <dd>{metadataEntity.fieldName}</dd>
            <dt>
              <span id="previousValue">
                <Translate contentKey="serviceNetApp.metadata.previousValue">Previous Value</Translate>
              </span>
            </dt>
            <dd>{metadataEntity.previousValue}</dd>
            <dt>
              <span id="replacementValue">
                <Translate contentKey="serviceNetApp.metadata.replacementValue">Replacement Value</Translate>
              </span>
            </dt>
            <dd>{metadataEntity.replacementValue}</dd>
            <dt>
              <span id="resourceClass">
                <Translate contentKey="serviceNetApp.metadata.resourceClass">Resource Class</Translate>
              </span>
            </dt>
            <dd>{metadataEntity.resourceClass}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.metadata.user">User</Translate>
            </dt>
            <dd>{metadataEntity.userLogin ? metadataEntity.userLogin : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/metadata" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/metadata/${metadataEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ metadata }: IRootState) => ({
  metadataEntity: metadata.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MetadataDetail);
