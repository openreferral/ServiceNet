import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './required-document.reducer';
import { IRequiredDocument } from 'app/shared/model/required-document.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRequiredDocumentDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RequiredDocumentDetail extends React.Component<IRequiredDocumentDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { requiredDocumentEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.requiredDocument.detail.title">RequiredDocument</Translate> [
            <b>{requiredDocumentEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="document">
                <Translate contentKey="serviceNetApp.requiredDocument.document">Document</Translate>
              </span>
            </dt>
            <dd>{requiredDocumentEntity.document}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.requiredDocument.srvc">Srvc</Translate>
            </dt>
            <dd>{requiredDocumentEntity.srvcName ? requiredDocumentEntity.srvcName : ''}</dd>
            <dt>
              <span id="externalDbId">
                <Translate contentKey="serviceNetApp.requiredDocument.externalDbId" />
              </span>
            </dt>
            <dd>{requiredDocumentEntity.externalDbId}</dd>
            <dt>
              <span id="providerName">
                <Translate contentKey="serviceNetApp.requiredDocument.providerName" />
              </span>
            </dt>
            <dd>{requiredDocumentEntity.providerName}</dd>
          </dl>
          <Button tag={Link} to="/entity/required-document" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/required-document/${requiredDocumentEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ requiredDocument }: IRootState) => ({
  requiredDocumentEntity: requiredDocument.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RequiredDocumentDetail);
