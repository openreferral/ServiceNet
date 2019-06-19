import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './exclusions-config.reducer';
import { IExclusionsConfig } from 'app/shared/model/exclusions-config.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IExclusionsConfigDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ExclusionsConfigDetail extends React.Component<IExclusionsConfigDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { exclusionsConfigEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.exclusionsConfig.detail.title">ExclusionsConfig</Translate> [
            <b>{exclusionsConfigEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <Translate contentKey="serviceNetApp.exclusionsConfig.account">Account</Translate>
            </dt>
            <dd>{exclusionsConfigEntity.accountName ? exclusionsConfigEntity.accountName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/exclusions-config" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/exclusions-config/${exclusionsConfigEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ exclusionsConfig }: IRootState) => ({
  exclusionsConfigEntity: exclusionsConfig.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ExclusionsConfigDetail);
