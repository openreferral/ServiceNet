import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './program.reducer';
import { IProgram } from 'app/shared/model/program.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProgramDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ProgramDetail extends React.Component<IProgramDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { programEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.program.detail.title">Program</Translate> [<b>{programEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="serviceNetApp.program.name">Name</Translate>
              </span>
            </dt>
            <dd>{programEntity.name}</dd>
            <dt>
              <span id="alternateName">
                <Translate contentKey="serviceNetApp.program.alternateName">Alternate Name</Translate>
              </span>
            </dt>
            <dd>{programEntity.alternateName}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.program.organization">Organization</Translate>
            </dt>
            <dd>{programEntity.organizationName ? programEntity.organizationName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/program" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/program/${programEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ program }: IRootState) => ({
  programEntity: program.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ProgramDetail);
