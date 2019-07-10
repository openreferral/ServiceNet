import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { byteSize, Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './phone.reducer';
import { IPhone } from 'app/shared/model/phone.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPhoneProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Phone extends React.Component<IPhoneProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { phoneList, match } = this.props;
    return (
      <div>
        <h2 id="phone-heading">
          <Translate contentKey="serviceNetApp.phone.home.title">Phones</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="serviceNetApp.phone.home.createLabel">Create new Phone</Translate>
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
                  <Translate contentKey="serviceNetApp.phone.number">Number</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.phone.extension">Extension</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.phone.type">Type</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.phone.language">Language</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.phone.description">Description</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.phone.location">Location</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.phone.srvc">Srvc</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.phone.organization">Organization</Translate>
                </th>
                <th>
                  <Translate contentKey="serviceNetApp.phone.contact">Contact</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {phoneList.map((phone, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${phone.id}`} color="link" size="sm">
                      {phone.id}
                    </Button>
                  </td>
                  <td>{phone.number}</td>
                  <td>{phone.extension}</td>
                  <td>{phone.type}</td>
                  <td>{phone.language}</td>
                  <td>{phone.description}</td>
                  <td>{phone.locationName ? <Link to={`location/${phone.locationId}`}>{phone.locationName}</Link> : ''}</td>
                  <td>{phone.srvcName ? <Link to={`service/${phone.srvcId}`}>{phone.srvcName}</Link> : ''}</td>
                  <td>{phone.organizationName ? <Link to={`organization/${phone.organizationId}`}>{phone.organizationName}</Link> : ''}</td>
                  <td>{phone.contactName ? <Link to={`contact/${phone.contactId}`}>{phone.contactName}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${phone.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${phone.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${phone.id}/delete`} color="danger" size="sm">
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

const mapStateToProps = ({ phone }: IRootState) => ({
  phoneList: phone.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Phone);
