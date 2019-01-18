import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './contact.reducer';
import { IContact } from 'app/shared/model/contact.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContactDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ContactDetail extends React.Component<IContactDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { contactEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="serviceNetApp.contact.detail.title">Contact</Translate> [<b>{contactEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="serviceNetApp.contact.name">Name</Translate>
              </span>
            </dt>
            <dd>{contactEntity.name}</dd>
            <dt>
              <span id="title">
                <Translate contentKey="serviceNetApp.contact.title">Title</Translate>
              </span>
            </dt>
            <dd>{contactEntity.title}</dd>
            <dt>
              <span id="department">
                <Translate contentKey="serviceNetApp.contact.department">Department</Translate>
              </span>
            </dt>
            <dd>{contactEntity.department}</dd>
            <dt>
              <span id="email">
                <Translate contentKey="serviceNetApp.contact.email">Email</Translate>
              </span>
            </dt>
            <dd>{contactEntity.email}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.contact.organization">Organization</Translate>
            </dt>
            <dd>{contactEntity.organizationName ? contactEntity.organizationName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.contact.srvc">Srvc</Translate>
            </dt>
            <dd>{contactEntity.srvcName ? contactEntity.srvcName : ''}</dd>
            <dt>
              <Translate contentKey="serviceNetApp.contact.serviceAtLocation">Service At Location</Translate>
            </dt>
            <dd>{contactEntity.serviceAtLocationId ? contactEntity.serviceAtLocationId : ''}</dd>
            <dt>
              <span id="externalDbId">
                <Translate contentKey="serviceNetApp.contact.externalDbId" />
              </span>
            </dt>
            <dd>{contactEntity.externalDbId}</dd>
            <dt>
              <span id="providerName">
                <Translate contentKey="serviceNetApp.contact.providerName" />
              </span>
            </dt>
            <dd>{contactEntity.providerName}</dd>
          </dl>
          <Button tag={Link} to="/entity/contact" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/contact/${contactEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ contact }: IRootState) => ({
  contactEntity: contact.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ContactDetail);
