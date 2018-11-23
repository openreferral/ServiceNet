import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './contact.reducer';
import { IContact } from 'app/shared/model/contact.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IContactProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Contact extends React.Component<IContactProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { contactList, match } = this.props;
    return (
      <div>
        <h2 id="contact-heading">
          <Translate contentKey="serviceNetApp.contact.home.title">Contacts</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.contact.home.createLabel">Create new Contact</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.contact.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.contact.title">Title</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.contact.department">Department</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.contact.email">Email</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.contact.organization">Organization</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.contact.srvc">Srvc</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.contact.serviceAtLocation">Service At Location</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {contactList.map((contact, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${contact.id}`} color="link" size="sm">
                      {contact.id}
                    </Button>
                  </td>
                  <td>{contact.name}</td>
                  <td>{contact.title}</td>
                  <td>{contact.department}</td>
                  <td>{contact.email}</td>
                  <td>
                    {contact.organizationName ? <Link to={`organization/${contact.organizationId}`}>{contact.organizationName}</Link> : ''}
                  </td>
                  <td>{contact.srvcName ? <Link to={`service/${contact.srvcId}`}>{contact.srvcName}</Link> : ''}</td>
                  <td>
                    {contact.serviceAtLocationId ? (
                      <Link to={`service-at-location/${contact.serviceAtLocationId}`}>{contact.serviceAtLocationId}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${contact.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${contact.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${contact.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ contact }: IRootState) => ({
  contactList: contact.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Contact);
